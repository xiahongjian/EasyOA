import request from '@/utils/request'

export function listProcDef(query) {
  return request({
    url: '/processes/definitions',
    method: 'get',
    params: query
  })
}
