import request from '@/utils/request'

// const BASE = '/dict/data'

// 获取字典的所有未禁用值
export function getDicts(dictKey) {
  return request({
    url: `/dict/dataEnable`,
    method: 'get',
    params: {
      dictKey
    }
  })
}

// // 获取字典所有值（包含禁用的）
// export function getDictValues(dictKey) {
//   return request({
//     url: `/dict/dataList`,
//     method: 'get',
//     params: {
//       dictKey
//     }
//   })
// }

export function listData(query) {
  return request({
    url: `/dict/dataList`,
    method: 'get',
    params: query
  })
}
