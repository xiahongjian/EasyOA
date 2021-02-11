import request from '@/utils/request'

const BASE = '/menu'

export function getRoutes() {
  return request({
    url: 'menu/menurole',
    method: 'get'
  })
}

export function listMenu(queryForm) {
  return request({
    url: '/menus',
    method: 'get',
    params: queryForm
  })
}

export function createMenu(data) {
  return request({
    url: BASE,
    method: 'post',
    data
  })
}

export function updateMenu(id, data) {
  return request({
    url: `${BASE}/${id}`,
    method: 'put',
    data
  })
}

export function getMenu(id) {
  return request({
    url: `${BASE}/${id}`,
    method: 'get'
  })
}

export function deleteMenu(id) {
  return request({
    url: `${BASE}/${id}`,
    method: 'delete'
  })
}
