<template>
  <basic-layout>
    <template #wrapper>
      <el-card class="box-card">
        <el-form ref="queryForm" :inline="true" :model="queryParams">
          <el-form-item label="Key" prop="key">
            <el-input
              v-model="queryParams.key"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="名称" prop="name">
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
        </el-row>

        <el-table
          ref="table"
          v-loading="loading"
          :data="records"
        >
          <!-- <el-table-column type="selection" width="55" align="center" /> -->
          <el-table-column type="expand">
            <template slot-scope="props">
              <el-row>
                <el-col :span="12">
                  <el-form label-position="right" label-width="80px">
                    <el-form-item label="Key" class="no-margin">
                      <span>{{ props.row.key }}</span>
                    </el-form-item>
                    <el-form-item label="名称" class="no-margin">
                      <span>{{ props.row.name }}</span>
                    </el-form-item>
                    <el-form-item label="描述" class="no-margin">
                      <span>{{ props.row.description }}</span>
                    </el-form-item>
                    <el-form-item label="版本" class="no-margin">
                      <span>{{ props.row.version }}</span>
                    </el-form-item>
                    <el-form-item label="备注" class="no-margin">
                      <span>{{ props.row.modelComment }}</span>
                    </el-form-item>
                  </el-form>
                </el-col>
                <el-col :span="12">
                  <el-form label-position="right" label-width="80px">
                    <el-form-item label="创建者" class="no-margin">
                      <span>{{ props.row.updatedByUser.name }}</span>
                    </el-form-item>
                    <el-form-item label="创建时间" class="no-margin">
                      <span>{{ props.row.createdAt }}</span>
                    </el-form-item>
                    <el-form-item label="更新者" class="no-margin">
                      <span>{{ props.row.updatedByUser.name }}</span>
                    </el-form-item>
                    <el-form-item label="更新时间" class="no-margin">
                      <span>{{ props.row.updatedAt }}</span>
                    </el-form-item>
                  </el-form>
                </el-col>
              </el-row>

            </template>
          </el-table-column>
          <el-table-column label="Key" prop="key" width="150" />
          <el-table-column label="名称" prop="name" :show-overflow-tooltip="true" />
          <el-table-column label="描述" prop="description" :show-overflow-tooltip="true" />
          <el-table-column label="版本" prop="version" align="center" />
          <el-table-column label="备注" prop="modelComment" :show-overflow-tooltip="true" />
          <el-table-column label="状态" prop="status" align="center" width="80">
            <template slot-scope="scope"><el-tag :type="getStatusTagType(scope.row.status)">{{ formatStatus(scope.row.status) }}</el-tag></template>
          </el-table-column>
          <!-- <el-table-column label="创建者" prop="createdByUser.name" />
          <el-table-column label="创建时间" align="center" prop="createdAt" width="200px" /> -->
          <el-table-column label="更新者" prop="updatedByUser.name" align="center" />
          <el-table-column label="更新时间" prop="updatedAt" width="200px" align="center" />
          <el-table-column label="操作" class-name="small-padding fixed-width" width="220" align="center">
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
              <action-group
                style="margin-left: 10px;"
                size="mini"
                type="text"
                :data="scope.row"
                :actions="moreActionCfg"
              />
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
          <el-form-item label="备注" prop="comment">
            <el-input v-model="form.comment" placeholder="请输入备注" type="textarea" :autosize="{ minRows: 6, maxRows: 6}" :maxlength="500" />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitForm">确定</el-button>
          <el-button @click="cancel">取消</el-button>
        </div>
      </el-dialog>

      <el-dialog title="流程图片" :visible.sync="imageDialogOpen" width="1000px" height="500px">
        <el-image :src="imageUrl">
          <div slot="placeholder" class="image-slot">
            加载中<span class="dot">...</span>
          </div>
        </el-image>
        <div slot="footer" class="dialog-footer">
          <el-button @click="closeImage">关闭</el-button>
        </div>
      </el-dialog>
    </template>
  </basic-layout>
</template>

<script>
import { listModel, createModel, deleteModel, updateModel, deployprocesses } from '@/api/process/model'
import ActionGroup from '@/components/ActionGroup'
export default {
  name: 'ProcessModel',
  components: {
    ActionGroup
  },
  data() {
    return {
      loading: true,
      isEdit: false,
      title: '',
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
            if (!this.form.file && this.id) {
              callback(new Error('上传文件不能为空'))
            }
            callback()
          },
          trigger: 'blur'
        }]
      },

      // image
      imageDialogOpen: false,
      imageUrl: undefined,

      moreActionCfg: [{
        text: '查看图片',
        icon: 'el-icon-picture-outline',
        handler: this.showImage
      }, {
        text: '下载XML',
        icon: 'el-icon-document',
        handler: this.downloadXML
      }, {
        text: '部署流程',
        icon: 'el-icon-coin',
        handler: this.deployProcess
      }]
    }
  },
  created() {
    this.handleQuery()
  },
  methods: {
    formatStatus(status) {
      if (status === 0) {
        return '未部署'
      } else if (status === 1) {
        return '已部署'
      }
    },
    getStatusTagType(status) {
      if (status === 0) {
        return 'warning'
      } else if (status === 1) {
        return 'success'
      }
    },
    handleQuery() {
      this.loading = true
      this.listModel()
    },
    handleUpdate(row) {
      this.reset()
      this.title = '更新流程模板'
      this.form = {
        id: row.id,
        comment: row.modelComment
      }
      this.open = true
    },
    handleCreate() {
      this.reset()
      this.title = '导入流程模板'
      this.open = true
    },
    handleDelete(row) {
      const id = row.id
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
            this.open = false
          } else {
            this.msgError(resp.message)
            this.open = false
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
    },
    showImage(record) {
      this.imageUrl = `processes/models/${record.id}/image`
      this.imageDialogOpen = true
    },
    closeImage() {
      this.imageDialogOpen = false
    },
    downloadXML(record) {
      const elink = document.createElement('a')
      elink.style.display = 'none'
      elink.target = '_blank'
      elink.href = `/processes/models/${record.id}/xml`
      document.body.appendChild(elink)
      elink.click()
      URL.revokeObjectURL(elink.href) // 释放URL 对象
      document.body.removeChild(elink)
    },
    deployProcess(record) {
      this.$confirm('是否确认部署此流程模板？', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return deployprocesses(record.id)
      }).then(() => {
        this.handleQuery()
        this.msgSuccess('部署成功')
      }).catch(() => {})
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
.expand-col {
  font-size: 0;
}
.expand-col label {
  width: 90px;
  color: #99a9bf;
}
.expand-col .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 50%;
}
.no-margin {
   margin-bottom: 0
}
</style>
