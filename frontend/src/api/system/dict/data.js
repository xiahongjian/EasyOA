import request from '@/utils/request'

const BASE = '/dict/data'

// 获取字典的所有未禁用值
export function getDicts(dictKey) {
  return request({
    url: `/dict/dataEnable`,
    method: 'get',
    params: {
      dictKey
    }
  })
}

// // 获取字典所有值（包含禁用的）
// export function getDictValues(dictKey) {
//   return request({
//     url: `/dict/dataList`,
//     method: 'get',
//     params: {
//       dictKey
//     }
//   })
// }

export function listData(query) {
  return request({
    url: `/dict/datas`,
    method: 'get',
    params: query
  })
}

export function getData(ids) {
  const id = ids instanceof Array && ids.length > 0 ? ids[0] : ids
  return request({
    url: `${BASE}/${id}`,
    method: 'get'
  })
}

export function updateData(id, form) {
  return request({
    url: `${BASE}/${id}`,
    method: 'put',
    data: form
  })
}

export function deleteData(id) {
  if (id instanceof Array && id.length > 1) {
    return request({
      url: `${BASE}/batch`,
      method: 'delete',
      params: {
        ids: id + ''
      }
    })
  }
  return request({
    url: `${BASE}/${id}`,
    method: 'delete'
  })
}

export function createData(form) {
  return request({
    url: `${BASE}`,
    method: 'post',
    data: form
  })
}
