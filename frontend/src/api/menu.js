import request from '@/utils/request'

export function getRoutes() {
  return request({
    url: 'menu/menurole',
    method: 'get'
  })
}
