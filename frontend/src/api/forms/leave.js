import request from '@/utils/request'

export function listLeaveForms(query) {
  return request({
    url: 'leaveForms',
    method: 'get',
    params: query
  })
}
