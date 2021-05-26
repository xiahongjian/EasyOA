<template>
  <basic-layout>
    <template #wrapper>
      <el-card class="box-card">
        <el-form ref="queryForm" :inline="true" :model="queryParams">
          <el-form-item label="关键字" prop="keyword">
            <el-input
              v-model="queryParams.keyword"
              placeholder="请输入姓名或邮箱"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="部门" prop="dept">
            <treeselect
              v-model="queryParams.dept"
              :options="deptOptions"
              :normalizer="normalizer"
              :show-count="true"
              placeholder="所属部门"
              style="width: 240px; height: 37px;"
              @keydup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <dict-select v-model="queryParams.status" placeholder="状态" :options="statusOptions" size="small" tyle="width: 240px" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
          <el-form-item label="测试">
            <user-picker
              v-model="queryParams.testUser"
              placeholder="选中用户"
              prop="testUser"
              :multi-select="true"
              display-field="email"
              trigger-text="Select"
            />
            {{ queryParams.testUser }}
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              v-permisaction="['sys:user:create']"
              type="primary"
              icon="el-icon-plus"
              size="mini"
              @click="handleCreate"
            >新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              v-permisaction="['sys:user:update']"
              type="success"
              icon="el-icon-edit"
              size="mini"
              :disabled="single"
              @click="handleUpdate"
            >修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              v-permisaction="['sys:user:delete']"
              type="danger"
              icon="el-icon-delete"
              size="mini"
              :disabled="multiple"
              @click="handleDelete"
            >删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              v-permisaction="['sys:user:export']"
              type="warning"
              icon="el-icon-download"
              size="mini"
              @click="handleExport"
            >导出</el-button>
          </el-col>
        </el-row>

        <el-table
          ref="table"
          v-loading="loading"
          :data="records"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55" align="center" />
          <!-- <el-table-column label="用户编号" prop="id" width="80" align="center" /> -->
          <el-table-column label="姓名" prop="name" width="90" />
          <el-table-column label="性别" prop="gender" align="center" width="80">
            <template slot-scope="scope">
              {{ genderFormat(scope.row) }}
            </template>
          </el-table-column>
          <el-table-column label="用户名" prop="username" :show-overflow-tooltip="true" width="150" />
          <el-table-column label="状态" prop="status" width="150px" align="center">
            <template slot-scope="scope">
              <status-tag :status="scope.row.status" />
            </template>
          </el-table-column>
          <el-table-column label="部门" prop="department" :show-overflow-tooltip="true" width="120" align="center" />
          <el-table-column label="邮箱" prop="email" :show-overflow-tooltip="true" />
          <!-- <el-table-column label="手机号码" prop="mobile" />
          <el-table-column label="创建时间" align="center" prop="createdAt" width="200px" /> -->
          <el-table-column label="更新时间" align="center" prop="updatedAt" width="200px" />
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="300">
            <template slot-scope="scope">
              <el-button
                v-permisaction="['sys:user:update']"
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
              >修改</el-button>
              <el-button
                v-permisaction="['sys:user:delete']"
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
              >删除</el-button>
              <el-dropdown style="margin-left: 10px;" @command="handleMoreAction">
                <el-button type="text" size="mini">
                  更多操作<i class="el-icon-arrow-down el-icon--right" />
                </el-button>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item
                    :command="buildCommand('handleShowInfo', scope.row)"
                    icon="el-icon-info"
                  >详情</el-dropdown-item>
                  <el-dropdown-item
                    v-permisaction="['sys:user:update']"
                    :command="buildCommand('handleResetPassword', scope.row)"
                    icon="el-icon-refresh"
                  >重置密码</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </template>
          </el-table-column>
        </el-table>
        <pagination
          v-show="total > 0"
          :total="total"
          :page.sync="queryParams.page"
          :limit.sync="queryParams.limit"
          @pagination="listUsers"
        />
      </el-card>

      <!-- dialog -->
      <el-dialog :title="title" :visible.sync="open" :close-on-click-modal="false" width="600px">
        <el-form ref="form" :model="form" :rules="rules" label-width="80px">
          <el-row>
            <el-col :span="12">
              <el-form-item label="姓名" prop="name">
                <el-input v-model="form.name" placeholder="请输入姓名" :disabled="isEdit" />
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item label="账号" prop="username">
                <el-input v-model="form.username" placeholder="请输入账号" :disabled="isEdit" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="性别" prop="gender">
                <dict-select v-model="form.gender" dict-type="sys_gender" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="状态" prop="status">
                <status-radio-group v-model="form.status" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="邮箱地址" prop="email">
                <el-input v-model="form.email" placeholder="请输入邮箱" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="手机号码" prop="mobile">
                <el-input v-model="form.mobile" placeholder="请输入手机号码" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="隶属部门" prop="departmentId">
                <treeselect
                  v-model="form.departmentId"
                  :options="deptOptions"
                  :normalizer="normalizer"
                  :show-count="true"
                  placeholder="请选择部门"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="岗位" prop="post">
                <dict-select v-model="form.post" placehodler="请选择岗位" dict-type="sys_post" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="角色" prop="roleIds" placeholder="请选择角色">
                <el-select v-model="form.roleIds" multiple collapse-tags>
                  <el-option
                    v-for="r in roleOptions"
                    :key="r.id"
                    :label="r.name"
                    :value="r.id"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitForm">确定</el-button>
          <el-button @click="cancel">取消</el-button>
        </div>
      </el-dialog>
    </template>
  </basic-layout>
</template>

<script>
import { mapGetters } from 'vuex'
import { createUser, getUser, listUsers, updateUser, deleteUser, resetPassword } from '@/api/sys/user'
import { listDept } from '@/api/sys/dept'
import { listAllRole } from '@/api/sys/role'
// import { validEmail, validMobile } from '@/utils/validate'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import DictSelect from '@/components/DictSelect'
import StatusTag from '@/components/StatusTag'
import StatusRadioGroup from '@/components/StatusRadioGroup'
import UserPicker from '@/components/UserPicker'

export default {
  name: 'User',
  components: { Treeselect, DictSelect, StatusTag, StatusRadioGroup, UserPicker },
  data() {
    return {
      loading: true,
      isEdit: false,
      open: false,
      infoOpen: false,
      title: '',
      ids: [],
      single: true,
      multiple: true,

      deptOptions: [],
      genderOptions: [],
      roleOptions: [],
      postOptions: [],

      queryParams: {
        testUser: 1,
        keyword: undefined,
        status: undefined,
        prop: undefined,
        order: undefined,
        dept: -1,
        page: 1,
        limit: 10
      },
      records: [],
      total: 0,

      form: {},
      userInfo: {},
      rules: {
        name: [{
          required: true, message: '姓名不能为空', trigger: 'blur'
        }],
        username: [{
          required: true, message: '账号不能为空', trigger: 'blur'
        }],
        gender: [{
          required: true, message: '性别不能为空', trigger: 'blur'
        }],
        email: [{
          required: true, message: '邮箱不能为空', trigger: 'blur'
        }, {
          type: 'email',
          trigger: ['blur', 'change'],
          message: '邮箱格式错误'
        }],
        mobile: [{
          required: true, message: '手机号码不能为空', trigger: 'blur'
        }, {
          pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
          message: '请输入正确的手机号码',
          trigger: ['blur', 'change']
        }]
      }
    }
  },
  computed: {
    ...mapGetters(['statusOptions'])
  },
  created() {
    this.getDicts('sys_gender').then(resp => {
      this.genderOptions = resp.data
    })
    this.getDicts('sys_post').then(resp => {
      this.postOptions = resp.data
    })
    this.listUsers()
    this.getDeptTreeselect()
  },
  methods: {
    listUsers() {
      const params = this.queryParams
      if (params.dept === -1) {
        params.dept = null
      }
      listUsers(params).then(resp => {
        const data = resp.data
        this.records = data.records
        this.total = data.total
        this.loading = false
      })
    },

    handleQuery() {
      this.loading = true
      this.listUsers()
    },
    handleCreate() {
      this.reset()
      this.open = true
      this.title = '新增用户'
      listAllRole().then(resp => {
        this.roleOptions = resp.data
      })
    },
    handleUpdate(row) {
      this.reset()
      const userId = row.id || this.ids[0]
      getUser(userId).then(resp => {
        this.form = resp.data
        this.open = true
        this.title = '修改用户'
        // 部门树初始化
      })
      listAllRole().then(resp => {
        this.roleOptions = resp.data
      })
    },
    handleDelete(row) {
      const id = row.id || this.ids
      this.$confirm(`是否确认删除用户编号为”${id}“的账号？`, '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return deleteUser(id)
      }).then(() => {
        this.listDept()
        this.msgSuccess('删除成功')
      }).catch(() => {})
    },
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (!valid) {
          return
        }
        const action = this.form.id ? (form) => updateUser(this.form.id, form) : createUser
        const msg = this.form.id ? '修改成功' : '新增成功'
        action(this.form).then(resp => {
          if (resp.success) {
            this.msgSuccess(msg)
            this.open = false
            this.listUsers()
          } else {
            this.msgError(resp.message)
          }
        })
      })
    },
    resetQuery() {
      this.resetForm('queryForm')
      this.queryParams.prop = undefined
      this.queryParams.order = undefined
      this.$refs.table.clearSort()
      this.handleQuery()
    },
    handleResetPassword(row) {
      this.$confirm(`是否确认重置用户名为"${row.username}"的用户密码？`, '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return resetPassword(row.id)
      }).then(() => {
        this.listUsers()
        this.msgSuccess('密码重置成功')
      }).catch(() => {})
    },
    handleShowInfo(row) {
      this.$router.push({
        name: 'UserInfo',
        params: {
          id: row.id
        }
      })
    },
    handleSort(data) {
      const { prop, order } = data
      this.queryParams.prop = prop
      this.queryParams.order = order === 'ascending' ? 'asc' : 'desc'
      this.handleQuery()
    },
    handleExport() {

    },
    reset() {
      this.form = {
        id: undefined,
        name: undefined,
        username: undefined,
        password: undefined,
        passwordReinput: undefined,
        deptmentId: undefined,
        email: undefined,
        mobile: undefined,
        gender: undefined,
        status: 1
      }
      this.resetForm('form')
    },
    cancel() {
      this.reset()
      this.open = false
    },
    getDeptTreeselect() {
      listDept().then(resp => {
        this.deptOptions = []
        const dept = {
          id: -1,
          name: '主类目',
          children: resp.data
        }
        this.deptOptions.push(dept)
      })
    },
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children
      }
      return {
        id: node.id,
        label: node.name,
        children: node.children
      }
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    genderFormat(row) {
      let label = '-'
      this.genderOptions.forEach(opt => {
        if (opt.value === row.gender) {
          label = opt.label
          return
        }
      })
      return label
    },
    buildCommand(command, data) {
      return {
        method: command,
        data
      }
    },
    handleMoreAction(command) {
      return this[command.method](command.data)
    }
  }
}
</script>
