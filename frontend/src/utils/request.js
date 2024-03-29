import axios from 'axios'
import { Message, MessageBox } from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'
// import router from '@/router'

// create an axios instance
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  // withCredentials: true, // send cookies when cross-domain requests
  timeout: 30000 // request timeout
})

// request interceptor
service.interceptors.request.use(
  config => {
    // do something before request is sent

    if (store.getters.token) {
      // let each request carry token
      // ['X-Token'] is a custom headers key
      // please modify it according to the actual situation
      config.headers['Authorization'] = getToken()
    }
    return config
  },
  error => {
    // do something with request error
    console.log(error) // for debug
    return Promise.reject(error)
  }
)

// response interceptor
service.interceptors.response.use(
  /**
   * If you want to get http information such as headers or status
   * Please return  response => response
  */

  /**
   * Determine the request status by custom code
   * Here is just an example
   * You can also judge the status by HTTP Status Code
   */
  response => {
    const res = response.data
    const oldToken = getToken()
    const token = response.headers['Authorization'] || response.headers['authorization'] // 后端返回header自动变为小写，此处做兼容处理
    // 刷新token
    if (token && oldToken !== token) {
      store.dispatch('user/refreshToken', token)
    }
    // if the custom code is not 20000, it is judged as an error.
    if (res.success !== true || res.code !== 20000) {
      // 30000: 未登录 30002: token过期 30006: token无效
      if (res.code === 30000 || res.code === 30002 || res.code === 30006) {
        // to re-login
        MessageBox.confirm(res.message, '注意', {
          confirmButtonText: '重新登录',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          store.dispatch('user/resetToken').then(() => {
            location.reload()
          })
        })
        return Promise.reject(new Error(res.message || 'Error'))
      }

      Message({
        message: res.message || 'Error',
        type: 'error',
        duration: 5 * 1000
      })
      return Promise.reject(new Error(res.message || 'Error'))
    } else {
      return res
    }
  },
  error => {
    console.log('err' + error) // for debug
    const msg = error.response && error.response.data && error.response.data.message || error.message
    Message({
      message: msg,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service
