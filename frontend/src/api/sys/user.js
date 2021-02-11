import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/login',
    method: 'post',
    data
  })
}

export function getInfo(token) {
  return request({
    url: '/user/userinfo',
    method: 'get'
  })
}

export function logout() {
  return request({
    url: '/logout',
    method: 'get'
  })
}

export function listUsers(query) {
  return request({
    url: '/users',
    method: 'get',
    params: query
  })
}

export function userSelectQuery(query) {
  return request({
    url: '/users',
    method: 'get',
    params: {
      keyword: query,
      status: 1,
      page: 0,
      limit: 20
    }
  })
}

export function getUser(id) {
  return request({
    url: `/user/${id}`,
    method: 'get'
  })
}
