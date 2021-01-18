import request from '@/utils/request'

const BASE = '/dict/value'

// 获取字典的所有未禁用值
export function getDicts(dictKey) {
  return request({
    url: `${BASE}/enable`,
    method: 'get',
    params: {
      dictKey
    }
  })
}

// 获取字典所有值（包含禁用的）
export function getDictValues(dictKey) {
  return request({
    url: `${BASE}/list`,
    method: 'get',
    params: {
      dictKey
    }
  })
}
