import request from '@/utils/request'

export function listModel(query) {
  return request({
    url: '/process/models',
    method: 'get',
    params: query
  })
}

function handleFormData(form) {
  const data = new FormData()
  data.append('file', form.file)
  data.append('comment', form.comment)
  return data
}

export function createModel(form) {
  return request({
    url: '/process/model',
    method: 'post',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: handleFormData(form)
  })
}

export function updateModel(id, form) {
  return request({
    url: `/process/meodel/${id}`,
    method: 'put',
    data: handleFormData(form)
  })
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
