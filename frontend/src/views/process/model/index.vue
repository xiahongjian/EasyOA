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
            >导入</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="success"
              icon="el-icon-refresh"
              size="mini"
              :disabled="single"
              @click="handleUpdate"
            >更新</el-button>
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
          <el-table-column type="expand">
            <template slot-scope="props">
              <el-form label-position="left" inline>
                <el-form-item label="流程ID">
                  <span>{{ props.row.modelId }}</span>
                </el-form-item>
                <el-form-item label="流程名称">
                  <span>{{ props.row.name }}</span>
                </el-form-item>
                <el-form-item label="描述">
                  <span>{{ props.row.description }}</span>
                </el-form-item>
                <el-form-item label="版本">
                  <span>{{ props.row.version }}</span>
                </el-form-item>
                <el-form-item label="备注">
                  <span>{{ props.row.comment }}</span>
                </el-form-item>
                <el-form-item label="创建者">
                  <span>{{ props.row.updatedByUser.name }}</span>
                </el-form-item>
                <el-form-item label="创建时间">
                  <span>{{ props.row.createdAt }}</span>
                </el-form-item>
                <el-form-item label="更新者">
                  <span>{{ props.row.updatedByUser.name }}</span>
                </el-form-item>
                <el-form-item label="更新时间">
                  <span>{{ props.row.updatedAt }}</span>
                </el-form-item>
              </el-form>
            </template>
          </el-table-column>
          <el-table-column label="流程ID" prop="modelId" width="150" />
          <el-table-column label="流程名称" prop="name" :show-overflow-tooltip="true" />
          <el-table-column label="描述" prop="description" :show-overflow-tooltip="true" />
          <el-table-column label="版本" prop="version" />
          <el-table-column label="备注" prop="comment" :show-overflow-tooltip="true" />
          <el-table-column label="创建者" prop="createdByUser.name" />
          <el-table-column label="创建时间" align="center" prop="createdAt" width="200px" />
          <el-table-column label="更新者" prop="updatedByUser.name" />
          <el-table-column label="更新时间" align="center" prop="updatedAt" width="200px" />
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="300">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-refresh"
                @click="handleUpdate(scope.row)"
              >更新</el-button>
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
                    icon="el-icon-document"
                  >查看XML</el-dropdown-item>
                  <el-dropdown-item
                    :command="buildCommand('showImage', scope.row)"
                    icon="el-icon-picture-outline"
                  >查看图片</el-dropdown-item>
                  <el-dropdown-item
                    :command="buildCommand('deployProcess', scope.row)"
                    icon="el-icon-coin"
                  >部署流程</el-dropdown-item>
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

      <el-dialog :title="title" :visible.sync="open" :close-on-click-modal="false" width="600px">
        <el-form ref="form" :model="form" label-position="top" :rules="rules">
          <el-form-item label="备注" prop="comment">
            <el-input v-model="form.comment" placeholder="请输入备注" type="textarea" :autosize="{ minRows: 6, maxRows: 6}" :maxlength="500" />
          </el-form-item>
          <el-form-item prop="file">
            <el-upload
              drag
              :multiple="false"
              :limit="1"
              accept="*.xml,*.bpmn"
              action="http://forRequired"
              :auto-upload="false"
              :on-change="handleSelectFile"
              :on-remove="handleSelectFile"
              :on-exceed="handleExceed"
              class="upload-field"
            >
              <i class="el-icon-upload" />
              <div class="el-upload__text" style="width: 100%">将文件拖到此处，或<em>点击上传</em></div>
              <div slot="tip" class="el-upload__tip">只能上传xml和bpmn文件</div>
            </el-upload>
          </el-form-item>
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
import { listModel, createModel, deleteModel, updateModel } from '@/api/process/model'
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
      open: false,

      queryParams: {
        modelId: undefined,
        name: undefined,
        page: 0,
        limit: 10
      },
      records: [],
      total: 0,

      form: {
        canUpload: true,
        comment: 'test'
      },
      rules: {
        file: [{
          validator: (rule, value, callback) => {
            if (!this.form.file) {
              callback(new Error('上传文件不能为空'))
            }
            callback()
          },
          trigger: 'blur'
        }]
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
      this.reset()
      this.title = '导入流程模板'
      this.open = true
    },
    handleDelete(row) {
      const id = row && row.id || this.ids
      this.$confirm('是否确认删除选择中的流程模板？', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return deleteModel(id)
      }).then(() => {
        this.handleQuery()
        this.msgSuccess('删除成功')
      }).catch(() => {})
    },
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (!valid) {
          return
        }
        this.$refs['form'].clearValidate()
        const action = this.form.id ? (form) => updateModel(this.form.id, form) : createModel
        const msg = this.form.id ? '更新成功' : '导入成功'
        action(this.form).then(resp => {
          if (resp.success) {
            this.msgSuccess(msg)
            this.handleQuery()
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
    reset() {
      this.form = {}
      this.resetForm('form')
    },
    cancel() {
      this.reset()
      this.open = false
    },
    buildCommand(command, data) {
      return {
        method: command,
        data
      }
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
    },
    handleSelectFile(file, fileList) {
      if (fileList && fileList.length > 0) {
        this.form.file = fileList[0].raw
      } else {
        this.form.file = undefined
      }
    },
    handleExceed(file, fileList) {
      this.msgInfo('每次只能上传一个文件。')
    }
  }
}
</script>

<style>
.upload-field .el-upload {
  width: 100%;
}
.upload-field .el-upload-dragger {
  width: 100%;
}
.able-expand label {
    width: 90px;
    color: #99a9bf;
  }
</style>
