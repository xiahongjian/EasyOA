import request from '@/utils/request'

export function listInstance(params) {
  return request({
    url: '/processes/instances',
    params,
    method: 'get'
  })
}
