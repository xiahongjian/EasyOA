<template>
  <basic-layout>
    <template #wrapper>
      <el-card class="box-card">
        <el-form ref="queryForm" :inline="true" :model="queryParams">
          <el-form-item label="流程ID" prop="modelId">
            <el-input
              v-model="queryParams.modelId"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="流程名称" prop="name">
            <el-input
              v-model="queryParams.name"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              type="primary"
              icon="el-icon-upload"
              size="mini"
              @click="handleCreate"
            >上传</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="success"
              icon="el-icon-edit"
              size="mini"
              :disabled="single"
              @click="handleUpdate"
            >修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="danger"
              icon="el-icon-delete"
              size="mini"
              :disabled="multiple"
              @click="handleDelete"
            >删除</el-button>
          </el-col>
        </el-row>

        <el-table
          ref="table"
          v-loading="loading"
          :data="records"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="流程ID" prop="modelId" width="150" />
          <el-table-column label="流程名称" prop="name" :show-overflow-tooltip="true" />
          <el-table-column label="描述" prop="description" :show-overflow-tooltip="true" />
          <el-table-column label="版本" prop="version" />
          <el-table-column label="创建者" prop="createdByUser.name" />
          <el-table-column label="创建时间" align="center" prop="createTime" width="200px" />
          <el-table-column label="更新者" prop="updatedByUser.name" />
          <el-table-column label="更新时间" align="center" prop="updateTime" width="200px" />
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="300">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
              >修改</el-button>
              <el-button
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
                    :command="buildCommand('showXML', scope.row)"
                    icon="el-icon-info"
                  >查看XML</el-dropdown-item>
                  <el-dropdown-item
                    :command="buildCommand('showImage', scope.row)"
                    icon="el-icon-refresh"
                  >查看图片</el-dropdown-item>
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
          @pagination="handleQuery"
        />
      </el-card>
    </template>
  </basic-layout>
</template>

<script>
import { listModel } from '@/api/process/model'
export default {
  name: 'ProcessModel',
  data() {
    return {
      loading: true,
      isEdit: false,
      title: '',
      ids: [],
      single: true,
      multiple: true,

      queryParams: {
        modelId: undefined,
        name: undefined,
        page: 0,
        limit: 10
      },
      records: [],
      total: 0,

      form: {},
      rules: {

      }
    }
  },
  created() {
    this.handleQuery()
  },
  methods: {
    handleQuery() {
      this.loading = true
      this.listModel()
    },
    handleUpdate(row) {

    },
    handleCreate() {

    },
    handleDelete(row) {

    },
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (!valid) {
          return
        }
        // const action = this.form.id ? (form) => updateUser(this.form.id, form) : createUser
        // const msg = this.form.id ? '修改成功' : '新增成功'
        // action(this.form).then(resp => {
        //   if (resp.success) {
        //     this.msgSuccess(msg)
        //     this.open = false
        //     this.listUsers()
        //   } else {
        //     this.msgError(resp.message)
        //   }
        // })
      })
    },
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    reset() {
      this.form = {}
      this.resetForm('form')
    },
    cancel() {
      this.reset()
      this.open = false
    },
    handleMoreAction(command) {
      return this[command.method](command.data)
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },

    listModel() {
      listModel(this.queryParams).then(resp => {
        const { data } = resp
        this.records = data.records
        this.total = data.total
        this.loading = false
      })
    }
  }
}
</script>

<style>

</style>
