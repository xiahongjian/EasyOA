import request from '@/utils/request'

export function getDicts(dictType) {
  return request({
    url: `/dict/type/${dictType}`,
    method: 'get'
  })
}
