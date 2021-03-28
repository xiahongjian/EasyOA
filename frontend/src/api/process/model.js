import request from '@/utils/request'

export function listModel(query) {
  return request({
    url: '/processes/models',
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
    url: '/processes/models',
    method: 'post',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: handleFormData(form)
  })
}

export function updateModel(id, form) {
  return request({
    url: `/processes/meodel/${id}`,
    method: 'put',
    data: handleFormData(form)
  })
}

export function deleteModel(id) {
  if (id instanceof Array) {
    return request({
      url: '/processes/modelss',
      method: 'delete',
      params: {
        ids: id + ''
      }
    })
  }

  return request({
    url: `/processes/models/${id}`,
    method: 'delete'
  })
}
/**
 * 部署流程模板
 * @param {Number} id 流程模板ID
 * @returns
 */
export function deployprocesses(id) {
  return request({
    url: `/processes/models/${id}/deploy`,
    method: 'put'
  })
}

export function getProcessImage(id) {
  return request({
    url: `/processes/models/${id}/image`,
    method: 'get'
  })
}
