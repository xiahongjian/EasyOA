<template>
  <basic-layout>
    <template #wrapper>
      <el-card class="box-card">
        <el-form ref="queryForm" :model="queryParams" :inline="true">
          <el-form-item label="角色名称" prop="name">
            <el-input
              v-model="queryParams.name"
              placeholder="请输入角色名称"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="权限字符" prop="key">
            <el-input
              v-model="queryParams.key"
              placeholder="请输入权限字符"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select
              v-model="queryParams.status"
              placeholder="角色状态"
              clearable
              size="small"
              style="width: 240px"
            >
              <el-option
                v-for="s in statusOptions"
                :key="s.value"
                :label="s.label"
                :value="s.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              v-permisaction="['sys:role:create']"
              type="primary"
              icon="el-icon-plus"
              size="mini"
              @click="handleCreate"
            >新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              v-permisaction="['sys:role:update']"
              type="success"
              icon="el-icon-edit"
              size="mini"
              :disabled="single"
              @click="handleUpdate"
            >修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              v-permisaction="['sys:role:delete']"
              type="danger"
              icon="el-icon-delete"
              size="mini"
              :disabled="multiple"
              @click="handleDelete"
            >删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              v-permisaction="['sys:role:export']"
              type="warning"
              icon="el-icon-download"
              size="mini"
              @click="handleExport"
            >导出</el-button>
          </el-col>
        </el-row>

        <el-table v-loading="loading" :data="records" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="角色编号" prop="id" width="120" />
          <el-table-column label="角色名称" prop="name" :show-overflow-tooltip="true" width="220" />
          <el-table-column label="权限字符" prop="key" :show-overflow-tooltip="true" width="220" />
          <el-table-column label="显示排序" prop="sort" width="100" />
          <el-table-column label="状态" align="center" width="100">
            <template slot-scope="scope">
              <el-switch
                v-model="scope.row.status"
                :active-value="1"
                :inactive-value="0"
                @change="handleStatusChange(scope.row)"
              />
            </template>
          </el-table-column>
          <el-table-column label="创建时间" align="center" prop="createTime" width="300" />
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button
                v-permisaction="['sys:role:update']"
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
              >修改</el-button>
              <el-button
                v-permisaction="['sys:role:delete']"
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
              >删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination
          v-show="total > 0"
          :total="total"
          :page.sync="queryParams.page"
          :limit.sync="queryParams.limit"
          @pagination="getList"
        />

        <!-- 添加、修改角色对话框 -->
        <el-dialog v-if="open" :title="title" :visible.sync="open" width="500px">
          <el-form ref="form" :model="form" :rules="rules" label-width="80px">
            <el-form-item label="角色名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入角色名称" :disabled="isEdit" />
            </el-form-item>
            <el-form-item label="权限字符" prop="key">
              <el-input v-model="form.key" placeholder="请输入权限字符" :disabled="isEdit" />
            </el-form-item>
            <el-form-item label="角色排序" prop="sort">
              <el-input-number v-model="form.sort" controls-position="right" :min="0" />
            </el-form-item>
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="s in statusOptions"
                  :key="s.value"
                  :label="s.value"
                >{{ s.label }}</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="菜单权限">
              <el-tree
                ref="menu"
                :data="menuOptions"
                show-checkbox
                node-key="id"
                :empty-text="menuOptionAlert"
                :props="defaultProps"
              />
            </el-form-item>
            <el-form-item label="备注">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
            </el-form-item>
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
import { mapGetters } from 'vuex'
import { listRole, getRole, createRole, updateRole, deleteRole, changeStatus, roleTreeSelect } from '@/api/role'
import { listMenu } from '@/api/menu'
export default {
  name: 'Role',
  data() {
    return {
      isEdit: false,
      ids: [],
      single: true,
      multiple: true,

      loading: false,
      open: false,
      title: '',

      menuOptions: [],
      deptOptions: [],
      menuOptionAlert: '加载中，请稍后',
      defaultProps: {
        children: 'children',
        label: 'title'
      },

      records: [],
      total: 0,
      queryParams: {
        page: 1,
        limit: 10,
        name: undefined,
        key: undefined,
        status: undefined
      },

      // form
      form: {},
      rules: {
        name: [{
          required: true, message: '角色名称不能为空', trigger: 'blur'
        }],
        key: [{
          required: true, message: '权限字符不能为空', trigger: 'blur'
        }],
        sort: [{
          required: true, message: '角色排序不能为空', trigger: 'blur'
        }]
      }
    }
  },
  computed: {
    ...mapGetters(['statusOptions'])
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      listRole(this.queryParams).then(resp => {
        const { data } = resp
        this.records = data.records
        this.total = data.total
      })
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleQuery() {
      this.page = 1
      this.getList()
    },
    handleCreate() {
      this.reset()
      this.getMenuTree()
      this.open = true
      this.title = '添加角色'
      this.isEdit = false
    },
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids[0]
      getRole(id).then(resp => {
        this.form = resp.data
        this.open = true
        this.title = '修改角色'
        this.getRoleMnueTreeSelect(id)
      })
    },
    handleDelete(row) {
      const id = row.id || this.ids
      this.$confirm('是否确认删除角色编号为"' + id + '"的数据？', '警告', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return deleteRole(id)
      }).then(() => {
        this.getList()
        this.$msgSuccess('删除成功')
      }).catch(() => {})
    },
    handleExport() {
      // TODO
    },
    handleStatusChange(row) {
      const text = row.status === 0 ? '停用' : '启用'
      this.$confirm(`确认要${text}"${row.name}"角色吗？`, '警告', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return changeStatus(row.id, row.status)
      }).then(() => {
        this.msgSuccess(`${text}成功`)
      }).catch(() => {
        row.status = row.status === 0 ? 1 : 0
      })
    },
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (!valid) {
          return
        }
        const isUpdate = this.form.id !== undefined
        const actionFunc = isUpdate
          ? (form) => updateRole(this.form.id, form)
          : createRole
        const successMsg = isUpdate ? '修改成功' : '新增成功'
        this.form.menuIds = this.getMenuAllCheckedKeys()
        actionFunc(this.form).then(resp => {
          if (resp.success === true) {
            this.msgSuccess(successMsg)
            this.open = false
            this.getList()
          } else {
            this.msgError(resp.message)
          }
        })
      })
    },
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    cancel() {
      this.reset()
      this.open = false
    },
    reset() {
      if (this.$refs.menu !== undefined) {
        this.$refs.menu.setCheckedKeys([])
      }

      this.form = {
        id: undefined,
        name: undefined,
        key: undefined,
        sort: 0,
        status: 1,
        menuIds: [],
        remark: undefined
      }
      this.resetForm('form')
    },
    // 返回菜单树
    getMenuTree() {
      return listMenu().then(resp => {
        this.menuOptions = resp.data
      })
    },
    // 获取选中的菜单节点
    getMenuAllCheckedKeys() {
      const helfCheckedKeys = this.$refs.menu.getHalfCheckedKeys()
      const checkedKeys = this.$refs.menu.getCheckedKeys()
      checkedKeys.push(...helfCheckedKeys)
      return checkedKeys
    },
    getRoleMnueTreeSelect(id) {
      this.getMenuTree().then(() => {
        return roleTreeSelect(id)
      }).then((resp) => {
        this.$nextTick(() => {
          this.$refs.menu.setCheckedKeys(resp.data || [])
        })
      })
    }
  }
}
</script>

<style>

</style>
