<template>
  <BasicLayout>
    <template #wrapper>
      <el-card class="box-card">
        <el-form ref="queryForm" :model="queryParams" :inline="true">
          <el-form-item label="字典名称" prop="name">
            <el-input
              v-model="queryParams.name"
              placeholder="请输入字典名称"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="字典类型" prop="key">
            <el-input
              v-model="queryParams.key"
              placeholder="请输入字典类型"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <dict-select v-model="queryParams.status" :options="statusOptions" size="small" style="width: 240px" />
          </el-form-item>

          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              v-permisaction="['sys:dicttype:add']"
              type="primary"
              icon="el-icon-plus"
              size="mini"
              @click="handleAdd"
            >新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              v-permisaction="['sys:dicttype:update']"
              type="success"
              icon="el-icon-edit"
              size="mini"
              :disabled="single"
              @click="handleUpdate"
            >修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              v-permisaction="['sys:dicttype:delete']"
              type="danger"
              icon="el-icon-delete"
              size="mini"
              :disabled="multiple"
              @click="handleDelete"
            >删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              v-permisaction="['sys:dicttype:export']"
              type="warning"
              icon="el-icon-download"
              size="mini"
              @click="handleExport"
            >导出</el-button>
          </el-col>
        </el-row>

        <el-table v-loading="loading" :data="records" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="字典编号" width="80" align="center" prop="id" />
          <el-table-column label="字典名称" align="center" prop="name" :show-overflow-tooltip="true" />
          <el-table-column label="字典类型" align="center" :show-overflow-tooltip="true">
            <template slot-scope="scope">
              <router-link :to="{name: 'DictData', params: {dictId: scope.row.id}}" class="link-type">
                <span>{{ scope.row.key }}</span>
              </router-link>
            </template>
          </el-table-column>
          <el-table-column label="状态" align="center">
            <template slot-scope="scope">
              <status-tag :status="scope.row.status" />
            </template>
          </el-table-column>
          <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" />
          <el-table-column label="创建时间" align="center" prop="createTime" width="180" />
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button
                v-permisaction="['sys:dicttype:update']"
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
              >修改</el-button>
              <el-button
                v-permisaction="['sys:dicttype:delete']"
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
              >删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination
          v-show="total>0"
          :total="total"
          :page.sync="queryParams.page"
          :limit.sync="queryParams.limit"
          @pagination="getList"
        />

        <el-dialog :title="title" :visible.sync="open" :close-on-click-modal="false" width="500px">
          <el-form ref="form" :model="form" :rules="rules" label-width="80px">
            <el-form-item label="字典名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入字典名称" :disabled="isEdit" />
            </el-form-item>
            <el-form-item label="字典类型" prop="key">
              <el-input v-model="form.key" placeholder="请输入字典类型" :disabled="isEdit" />
            </el-form-item>
            <el-form-item label="状态" prop="status">
              <status-radio-group v-model="form.status" />
            </el-form-item>
            <el-form-item label="备注" prop="remark">
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
  </BasicLayout>
</template>
<script>
import { mapGetters } from 'vuex'
import { listType, updateType, addType, delType, getType } from '@/api/sys/dict/type'
import StatusRadioGroup from '@/components/StatusRadioGroup'
import StatusTag from '@/components/StatusTag'
import DictSelect from '@/components/DictSelect'

export default {
  name: 'Dict',
  components: {
    StatusTag,
    StatusRadioGroup,
    DictSelect
  },
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      total: 0,
      records: [],
      title: '',
      isEdit: false,
      // 是否显示弹出层
      open: false,

      queryParams: {
        page: 1,
        limit: 10,
        name: undefined,
        key: undefined,
        status: undefined
      },
      form: {},
      rules: {
        name: [
          { required: true, message: '字典名称不能为空', trigger: 'blur' }
        ],
        key: [
          { required: true, message: '字典类型不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    ...mapGetters(['statusOptions'])
  },
  created() {
    this.getList()
    this.reset()
  },
  methods: {
    getList() {
      this.loading = true
      listType(this.queryParams).then(resp => {
        const { data } = resp
        this.records = data.records
        this.total = data.total
        this.loading = false
      })
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    handleQuery() {
      this.queryParams.page = 1
      this.getList()
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加字典类型'
      this.isEdit = false
    },
    handleUpdate(row) {
      this.reset()
      const dictId = row.id || this.ids
      getType(dictId).then(resp => {
        this.form = resp.data
        this.open = true
        this.title = '修改字典类型'
        this.isEdit = true
      })
    },
    handleDelete(row) {
      const dictIds = row.id || this.ids
      this.$confirm(`是否确认删除字典编号为”${dictIds}“的数据项？`, '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return delType(dictIds)
      }).then(() => {
        this.getList()
        this.msgSuccess('删除成功')
      }).catch(() => {})
    },
    handleExport() {

    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    reset() {
      this.form = {
        id: undefined,
        key: undefined,
        type: undefined,
        status: 1,
        remark: undefined
      }
      this.resetForm('form')
    },
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (!valid) {
          return
        }
        if (this.form.id !== undefined) {
          updateType(this.form.id, this.form).then(resp => {
            if (resp.success) {
              this.msgSuccess('修改成功')
              this.open = false
              this.getList()
            } else {
              this.msgError(resp.message)
            }
          })
        } else {
          addType(this.form).then(resp => {
            if (resp.success) {
              this.msgSuccess('新增成功')
              this.open = false
              this.getList()
            } else {
              this.msgError(resp.message)
            }
          })
        }
      })
    },
    cancel() {
      this.open = false
      this.reset()
    }
  }
}
</script>
