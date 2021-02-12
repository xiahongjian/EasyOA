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

function handleDept(param) {
  const data = Object.assign({}, param)
  // 处理所属部门为最上级的情况
  if (data.departmentId === -1) {
    data.departmentId = null
  }
  return data
}

export function createUser(param) {
  return request({
    url: '/user',
    method: 'post',
    data: handleDept(param)
  })
}

export function updateUser(id, param) {
  return request({
    url: `/user/${id}`,
    method: 'put',
    data: handleDept(param)
  })
}

export function deleteUser(id) {
  if (id instanceof Array) {
    return request({
      url: '/users',
      method: 'delete',
      params: {
        ids: id + ''
      }
    })
  }

  return request({
    url: `/user/${id}`,
    method: 'delete'
  })
}

export function resetPassword(id) {
  return request({
    url: `/user/{id}/reset`,
    method: 'put'
  })
}
