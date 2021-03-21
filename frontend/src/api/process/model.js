import request from '@/utils/request'

export function listModel(query) {
  return request({
    url: '/process/models',
    method: 'get',
    params: query
  })
}

export function createModel(form) {
  return request({
    url: '/process/model',
    method: 'post',
    data: form
  })
}

export function updateModel(form) {

}

export function deleteModel(id) {
  if (id instanceof Array) {
    return request({
      url: '/process/models',
      method: 'delete',
      params: {
        ids: id + ''
      }
    })
  }

  return request({
    url: `/process/model/${id}`,
    method: 'delete'
  })
}
/**
 * 部署流程模板
 * @param {Number} id 流程模板ID
 * @returns
 */
export function deployProcess(id) {
  return request({
    url: `/process/model/${id}/deploy`,
    method: 'put'
  })
}
