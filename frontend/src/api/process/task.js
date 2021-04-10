import request from '@/utils/request'

export function listTask(params) {
  return request({
    url: '/processes/tasks',
    params,
    method: 'get'
  })
}

export function listMyTask(params) {
  return request({
    url: '/processes/tasks/mine',
    params,
    method: 'get'
  })
}

export function completeTask(taskId, action, comment) {
  return request({
    url: `/processes/tasks/${taskId}/complete`,
    params: {
      action,
      comment
    },
    method: 'put'
  })
}

export function reassignTask(taskId, userId, comment) {
  return request({
    url: `/processes/tasks/${taskId}/reassign`,
    params: {
      userId,
      comment
    },
    method: 'put'
  })
}
