import request from '@/utils/request'

export function listProcDef(query) {
  return request({
    url: '/processes/definitions',
    method: 'get',
    params: query
  })
}

export function suspendProcDef(procDefId) {
  return request({
    url: `/processes/definitions/${encodeURIComponent(procDefId)}/suspend`,
    method: 'get'
  })
}

export function activeProcDef(procDefId) {
  return request({
    url: `/processes/definitions/${encodeURIComponent(procDefId)}/active`,
    method: 'get'
  })
}

export function deleteProcDef(procDefId) {
  return request({
    url: `/processes/definitions/${encodeURIComponent(procDefId)}`,
    method: 'delete'
  })
}
