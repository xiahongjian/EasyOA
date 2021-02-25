<template>
  <basic-layout>
    <template #wrapper>
      <el-card class="box-card">

        <!-- query表单 -->
        <el-form ref="queryForm" :model="queryParams" :inline="true" label-width="68px">
          <el-form-item label="菜单名称" prop="query">
            <el-input
              v-model="queryParams.query"
              placeholder="请输入菜单名称"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="状态" prop="visible">
            <el-select
              v-model="queryParams.visible"
              placeholder="状态"
              clearable
              size="small"
              style="width: 240px"
            >
              <el-option
                v-for="s in visibleOptions"
                :key="s.id"
                :label="s.label"
                :value="s.value"
              />
            </el-select>
          </el-form-item>

          <!-- 按钮 -->
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            <el-button
              v-permisaction="['sys:menu:create']"
              type="primary"
              icon="el-icon-plus"
              size="mini"
              @click="handleCreate"
            >新增</el-button>
          </el-form-item>
        </el-form>

        <!-- 数据表格 -->
        <el-table
          v-loading="loading"
          :data="records"
          row-key="id"
          :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
        >
          <el-table-column prop="title" label="菜单名称" :show-overflow-tooltip="true" width="220px" />
          <el-table-column prop="icon" label="图标" align="center" width="100px">
            <template slot-scope="scope">
              <svg-icon :icon-class="scope.row.icon" />
            </template>
          </el-table-column>
          <el-table-column prop="sort" label="排序" align="center" width="60px" />
          <el-table-column prop="permission" label="权限标识" :show-overflow-tooltip="true" />
          <el-table-column prop="routePath" label="路径" :show-overflow-tooltip="true">
            <template slot-scope="scope">
              <el-tag
                v-if="scope.row.type == 4 && scope.row.routePath"
                type="primay"
              >{{ scope.row.method ? (scope.row.method + ' ' + scope.row.routePath) : scope.row.routePath }}</el-tag>
              <span v-else>{{ scope.row.component }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="visible" label="可见" :formatter="visibleFormat" width="80">
            <template slot-scope="scope">
              <el-tag
                :type="scope.row.visible === false ? 'danger' : 'success'"
                disable-transitions
              >{{ visibleFormat(scope.row) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="创建时间" align="center" prop="createTime" width="180" />
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="220">
            <template slot-scope="scope">
              <el-button
                v-permisaction="['sys:menu:update']"
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
              >修改</el-button>
              <el-button
                v-permisaction="['sys:menu:create']"
                size="mini"
                type="text"
                icon="el-icon-plus"
                @click="handleCreate(scope.row)"
              >新增</el-button>
              <el-button
                v-permisaction="['sys:menu:delete']"
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
              >删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 创建、更新对话框 -->
        <el-dialog :title="title" :visible.sync="open" :close-on-click-modal="false" width="600px">
          <el-form ref="form" :model="form" :rules="rules" label-width="80px">
            <el-row>
              <el-col :span="24">
                <el-form-item label="上级菜单" prop="parentId">
                  <treeselect
                    v-model="form.parentId"
                    :options="menuOptions"
                    :normalizer="normalizer"
                    :show-count="true"
                    placeholder="选择上级菜单"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="菜单标题" prop="title">
                  <el-input v-model="form.title" placeholder="请输入菜单标题" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="显示排序" prop="sort">
                  <el-input-number v-model="form.sort" controls-position="right" :min="0" />
                </el-form-item>
              </el-col>

              <el-col :span="24">
                <el-form-item label="菜单类型" prop="type">
                  <el-radio-group v-model="form.type">
                    <el-radio
                      v-for="type in menuTypeOptions"
                      :key="type.value"
                      :label="type.value"
                    >{{ type.label }}</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item v-if="form.type == 4" label="请求方式">
                  <el-radio-group v-model="form.method">
                    <el-radio label="GET">GET</el-radio>
                    <el-radio label="POST">POST</el-radio>
                    <el-radio label="PUT">PUT</el-radio>
                    <el-radio label="DELETE">DELETE</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="菜单图标">
                  <el-popover
                    placement="buttom-start"
                    width="460"
                    trigger="click"
                    @show="$refs['iconSelect'].reset()"
                  >
                    <icon-select ref="iconSelect" @selected="selected" />
                    <el-input slot="reference" v-model="form.icon" placeholder="点击选择图标" readonly>
                      <svg-icon
                        v-if="form.icon"
                        slot="prefix"
                        :icon-class="form.icon"
                        class="el-input__icon"
                        style="height: 32px; width: 16px;"
                      />
                      <i v-else slot="prefix" class="el-icon-search el-input__icon" />
                    </el-input>
                  </el-popover>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item v-if="isMenuOrDir" label="路由名称" prop="routeName">
                  <el-input v-model="form.routeName" placeholder="请输入路由名称" />
                </el-form-item>
              </el-col>

              <el-col v-if="isMenuOrDir" :span="12">
                <el-form-item label="组件路径" prop="component">
                  <el-input v-model="form.component" placeholder="请输入组件路径" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item v-if="isMenuOrDir" label="是否外链">
                  <el-radio-group v-model="form.externalLink">
                    <el-radio :label="true">是</el-radio>
                    <el-radio :label="false">否</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>

              <el-col :span="12">
                <el-form-item v-if="!isButton" label="路由地址" prop="routePath">
                  <el-input v-model="form.routePath" placeholder="请输入路由地址" />
                </el-form-item>
              </el-col>

              <el-col :span="12">
                <el-form-item v-if="isMenu || isButton" label="权限标识">
                  <el-input v-model="form.permission" placeholder="权限标识" maxlength="50" />
                </el-form-item>
              </el-col>

              <el-col :span="24">
                <el-form-item v-if="!isButton" label="菜单状态">
                  <el-radio-group v-model="form.visible">
                    <el-radio
                      v-for="o in visibleOptions"
                      :key="o.value"
                      :label="o.value"
                    >{{ o.label }}</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button type="primary" @click="submitForm">确定</el-button>
            <el-button @click="cancel">取消</el-button>
          </div>
        </el-dialog>
      </el-card>
    </template>
  </basic-layout>
</template>
<script>
import IconSelect from '@/components/IconSelect'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import { mapGetters } from 'vuex'
import { createMenu, deleteMenu, getMenu, listMenu, /*, createMenu, updateMenu, deleteMenu, getMenu*/
  updateMenu } from '@/api/sys/menu'
export default {
  name: 'Menu',
  components: { IconSelect, Treeselect },
  data() {
    return {
      loading: false,
      records: [],
      menuOptions: [],

      title: '',
      isEdit: false,
      open: false,

      queryParams: {
        query: undefined,
        status: undefined
      },
      form: {},
      rules: {
        title: [
          { required: true, message: '菜单标题不能为空', trigger: 'blur' }
        ],
        sort: [
          { required: true, message: '菜单排序不能为空', trigger: 'blur' }
        ],
        parentId: [
          { required: true, message: '父级菜单不能为空', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              if (this.form.id === value) {
                return callback(new Error('不能以自己作为父级菜单'))
              }
              callback()
            },
            trigger: 'blur'
          }
        ]
      }
    }
  },
  computed: {
    ...mapGetters(['visibleOptions', 'menuTypeOptions']),
    isMenuOrDir() {
      return this.form.type === 1 || this.form.type === 2
    },
    isDir() {
      return this.form.type === 1
    },
    isMenu() {
      return this.form.type === 2
    },
    isButton() {
      return this.form.type === 3
    },
    isInterface() {
      return this.form.type === 4
    }
  },
  created() {
    this.listMenu()
  },
  methods: {
    listMenu() {
      this.loading = true
      listMenu(this.queryParams).then(resp => {
        const { data } = resp
        this.records = data
        this.loading = false
      })
    },
    handleQuery() {
      this.listMenu()
    },

    handleCreate(row) {
      this.rest()
      this.getTreeSelect()
      if (row != null) {
        this.form.parentId = row.id
      }
      this.open = true
      this.title = '添加菜单'
    },

    handleUpdate(row) {
      this.rest()
      this.getTreeSelect()
      getMenu(row.id).then(resp => {
        this.form = resp.data
        if (this.form.parentId === null) {
          this.form.parentId = -1 // 设置目录层次
        }
        this.open = true
        this.title = '修改菜单'
      })
    },

    handleDelete(row) {
      this.$confirm(`是否确认删除名称为${row.title}的数据项？`, '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(() => {
        return deleteMenu(row.id)
      }).then(() => {
        this.listMenu()
        this.msgSuccess('删除成功')
      }).catch(() => {})
    },
    // 图标选择
    selected(name) {
      this.form.icon = name
    },

    submitForm() {
      this.$refs['form'].validate(valid => {
        if (!valid) {
          return
        }
        const isUpdate = this.form.id !== undefined
        const api = isUpdate ? (form) => updateMenu(this.form.id, form) : createMenu
        const successMsg = isUpdate ? '修改成功' : '新增成功'
        api(this.form).then(resp => {
          if (resp.success === true) {
            this.msgSuccess(successMsg)
            this.open = false
            this.listMenu()
          } else {
            this.msgError(resp.message)
          }
        })
      })
    },

    // 表单重置
    rest() {
      this.form = {
        menuId: undefined,
        parentId: null,
        title: undefined,
        icon: undefined,
        type: 2,
        sort: 0,
        permission: undefined,
        visible: true,
        method: undefined,
        component: undefined,
        routeName: undefined,
        routePath: undefined,
        externalLink: false
      }
      this.resetForm('form')
    },

    visibleFormat(row) {
      // 如果是按钮则显示 ‘-- --’
      if (row.type === 3) {
        return '-- --'
      }
      return this.selectDictLabel(this.visibleOptions, row.visible)
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    getTreeSelect() {
      listMenu().then(resp => {
        this.menuOptions = []
        const menu = {
          id: -1,
          title: '主类目',
          children: resp.data
        }
        this.menuOptions.push(menu)
      })
    },
    /** 转换菜单数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children
      }
      return {
        id: node.id,
        label: node.title,
        children: node.children
      }
    },
    cancel() {
      this.open = false
      this.rest()
    }
  }

}
</script>
