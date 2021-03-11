import request from '@/utils/request'

export function listLeaveForms(query) {
  request({
    url: 'leaveForms',
    method: 'get'
  })
}
