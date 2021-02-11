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
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

export default {
  name: 'User',
  components: { Treeselect },
  data() {
    return {
      loading: true,
      idEdit: false,
      open: false,
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

      form: {}
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
        params.dept == null
      }
      listUsers(this.queryParams).then(resp => {
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
      const userId = row ? row.id : this.ids[0]
      getUser(userId).then(resp => {
        this.form = resp.data
        this.open = true
        this.title = '修改用户'
        // 部门树初始化
      })
    },
    handleDelete(row) {

    },
    submitForm() {

    },
    resetQuery() {
      this.restForm('queryForm')
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
    }
  }
}
</script>
