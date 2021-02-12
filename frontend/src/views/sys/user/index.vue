<template>
  <basic-layout>
    <template #wrapper>
      <el-card class="box-card">
        <el-form ref="queryForm" :inline="true">
          <el-form-item label="关键字">
            <el-input
              v-model="queryParams.keyword"
              placeholder="请输入姓名或邮箱"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="部门">
            <treeselect
              v-model="queryParams.dept"
              :options="deptOptions"
              :normalizer="normalizer"
              :show-count="true"
              placeholder="所属部门"
              size="small"
              style="width: 240px"
              @keydup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="queryParams.status" placeholder="状态" clearable size="small">
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
          v-loading="loading"
          :data="records"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="姓名" prop="name" width="90" />
          <el-table-column label="性别" prop="gender" align="center" width="80">
            <template slot-scope="scope">
              {{ genderFormat(scope.row) }}
            </template>
          </el-table-column>
          <el-table-column label="用户名" prop="username" :show-overflow-tooltip="true" width="150" />
          <el-table-column prop="status" label="状态" width="150px" align="center">
            <template slot-scope="scope">
              <el-tag
                :type="scope.row.status === 0 ? 'danger' : 'success'"
                disable-transitions
              >{{ statusFormat(scope.row) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="部门" prop="department" :show-overflow-tooltip="true" width="120" align="center" />
          <el-table-column label="邮箱" prop="email" :show-overflow-tooltip="true" />
          <!-- <el-table-column label="手机号码" prop="mobile" /> -->
          <!-- <el-table-column label="创建时间" align="center" prop="createTime" width="200px" /> -->
          <el-table-column label="更新时间" align="center" prop="updateTime" width="200px" />
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="250">
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
              <el-button
                size="mini"
                type="text"
                icon="el-icon-info"
                @click="handleShowInfo(scope.row)"
              >详情
              </el-button>
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

    </template>
  </basic-layout>
</template>

<script>
import { mapGetters } from 'vuex'
import { getDicts } from '@/api/sys/dict/data'
import { getUser, listUsers } from '@/api/sys/user'
import { listDept } from '@/api/sys/dept'
import Treeselect from '@riophae/vue-treeselect'
import { /* createUser, updateUser,*/ deleteUser, resetPassword } from '@/api/sys/user'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

export default {
  name: 'User',
  components: { Treeselect },
  data() {
    return {
      loading: true,
      idEdit: false,
      open: false,
      infoOpen: false,
      title: '',
      ids: [],
      single: true,
      multiple: true,

      deptOptions: [],
      genderOpts: [],

      queryParams: {
        keyword: undefined,
        status: undefined,
        dept: -1,
        page: 1,
        limit: 10
      },
      records: [],
      total: 0,

      form: {},
      userInfo: {}
    }
  },
  computed: {
    ...mapGetters(['statusOptions'])
  },
  created() {
    getDicts('sys_gender').then(resp => {
      this.genderOpts = resp.data
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
    },
    handleDelete(row) {
      this.$confirm(`是否确认删除部门"${row.name}"？`, '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return deleteUser(row.id)
      }).then(() => {
        this.listDept()
        this.msgSuccess('删除成功')
      }).catch(() => {})
    },
    submitForm() {

    },
    resetQuery() {
      this.restForm('queryForm')
      this.handleQuery()
    },
    handleRestPassword() {
      this.$confirm(`是否确认重置用户名为"${this.userInfo.username}"的用户密码？`, '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return resetPassword(this.userInfo.id)
      }).then(() => {
        this.listUsers()
        this.msgSuccess('删除成功')
      }).catch(() => {})
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
      this.rest()
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
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status)
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
    handleSelectionChange(row) {

    },
    genderFormat(row) {
      let label = '-'
      this.genderOpts.forEach(opt => {
        if (opt.value === row.gender) {
          label = opt.label
          return
        }
      })
      return label
    }
  }
}
</script>
