<template>
  <basic-layout>
    <template #wrapper>
      <el-card class="box-card">
        <!-- <el-form ref="queryForm" :model="queryParams" :inline="true">
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
              clear
              size="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select
              v-model="queryParams.status"
              placeholder="角色状态"
              clearable,
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
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form-item>
        </el-form> -->

        <!-- <el-row :gutter="10" class="mb8">
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
        </el-row> -->

        <!-- <el-table v-loading="loading" :data="records" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="角色编号" prop="id" width="120" />
          <el-table-column label="角色名称" prop="name" :show-overflow-tooltip="true" width="150" />
          <el-table-column label="权限字符" prop="key" :show-overflow-tooltip="true" width="150" />
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
          <el-table-column label="创建时间" align="center" prop="createTime" width="100" />
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
                @click="handleDelete(scope.row.id)"
              >删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination
          v-show="total > 0"
          :total="total"
          :page.sync="queryParams.page"
          :limit.sync="queryForm.limit"
          @pagination="getList"
        /> -->
      </el-card>
    </template>
  </basic-layout>
</template>

<script>
import { mapGetters } from 'vuex'
import { listRole, getRole, createRole, updateRole, deleteRole } from '@/api/role'
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

      records: [],
      total: undefined,
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
      this.open = true
      this.title = '添加角色'
      this.isEdit = false
    },
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getRole(id).then(resp => {
        this.form = resp.data
        this.open = true
        this.title = '修改角色'
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

    },
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          return
        }
        const isUpdate = this.form.id !== undefined
        const actionFunc = isUpdate
          ? (form) => updateRole(this.form.id, form)
          : createRole
        const successMsg = isUpdate ? '修改成功' : '新增成功'
        actionFunc(this.form).then(resp => {
          if (resp.success === true) {
            this.$msgSuccess(successMsg)
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
      // TODO

      this.form = {
        id: undefined,
        name: undefined,
        key: undefined,
        sort: 0,
        status: 0,
        remark: undefined
      }
    }
  }
}
</script>

<style>

</style>
