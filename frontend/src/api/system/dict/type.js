import request from '@/utils/request'

export function listType(query) {
  return request({
    url: '/dict/type/list',
    method: 'get',
    params: query
  })
}
