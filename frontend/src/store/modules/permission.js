import Layout from '@/layout'
import { asyncRoutes, constantRoutes } from '@/router'
import { getRoutes } from '@/api/menu'

/**
 * Use meta.role to determine if the current user has permission
 * @param roles
 * @param route
 */
function hasPermission(roles, route) {
  if (route.meta && route.meta.roles) {
    return roles.some(role => route.meta.roles.includes(role))
  } else {
    return true
  }
}

function hasPathPermission(pathes, route) {
  if (route.path) {
    return pathes.some(path => route.path === path.path)
  } else {
    return true
  }
}

/**
 * Filter asynchronous routing tables by recursion
 * @param routes asyncRoutes
 * @param roles
 */
export function filterAsyncRoutes(routes, roles) {
  const res = []

  routes.forEach(route => {
    const tmp = { ...route }
    if (hasPermission(roles, tmp)) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, roles)
      }
      res.push(tmp)
    }
  })

  return res
}

export function filterAsyncPathRoutes(routes, paths) {
  const res = []

  routes.forEach(route => {
    const tmp = { ...route }
    if (hasPathPermission(paths, tmp)) {
      if (tmp.children) {
        tmp.children = filterAsyncPathRoutes(tmp.children, paths)
      }
      res.push(tmp)
    }
  })

  return res
}

export function generateMenu(routes, data) {
  data.forEach(item => {
    const menu = {
      path: item.routePath,
      component: item.component === 'Layout' ? Layout : loadView(item.component),
      hidden: item.visible !== true,
      children: [],
      name: item.routeName,
      meta: {
        title: item.title,
        icon: item.icon,
        noCache: true
      }
    }
    if (item.children) {
      generateMenu(menu.children, item.children)
    }
    routes.push(menu)
  })
}

export const loadView = (view) => {
  return (resolve) => require(['@/views' + view], resolve)
}

const state = {
  routes: [],
  addRoutes: []
}

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes
    state.routes = constantRoutes.concat(routes)
  }
}

const actions = {
  generateRoutes({ commit }, roles) {
    return new Promise(resolve => {
      const loadMenuData = []

      getRoutes().then(resp => {
        const { data } = resp
        Object.assign(loadMenuData, data)

        generateMenu(asyncRoutes, loadMenuData)
        asyncRoutes.push({ path: '*', redirect: '/', hidden: true })
        commit('SET_ROUTES', asyncRoutes)
        resolve(asyncRoutes)
      })
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
