<template>
  <basic-layout>
    <template #wrapper>
      <el-card class="box-card">
        <el-form ref="queryForm" :inline="true" :model="queryParams" label-width="68px">
          <el-form-item label="假期类型" prop="type">
            <el-select
              v-model="queryParams.type"
              placeholder="请选择假期类型"
              clearable
              size="small"
              style="width: 240px"
            >
              <el-option
                v-for="o in leaveTypeOpts"
                :key="o.id"
                :label="o.label"
                :value="o.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="创建者" prop="creatorId">
            <el-select
              v-model="queryParams.creatorId"
              value-key="id"
              placeholder="请选择创建者"
              clearable
              filterable
              remote
              :default-first-option="true"
              :remote-method="selectUser"
              :loading="userSelectLoading"
              size="small"
              style="width: 240px"
            >
              <el-option
                v-for="o of userSelectOpts"
                :key="o.id"
                :label="o.name"
                :value="o.id"
              >
                <span>{{ o.name }} ({{ o.email }})</span>
              </el-option>
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
              v-permisaction="['form:leaveform:create']"
              type="primary"
              icon="el-icon-plus"
              size="mini"
              @click="handleAdd"
            >新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              v-permisaction="['form:leaveform:update']"
              type="success"
              icon="el-icon-edit"
              size="mini"
              :disabled="single"
              @click="handleUpdate"
            >修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              v-permisaction="['form:leaveform:delete']"
              type="danger"
              icon="el-icon-delete"
              size="mini"
              :disabled="multiple"
              @click="handleDelete"
            >删除</el-button>
          </el-col>
        </el-row>

        <el-table v-loading="loading" :data="records" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="假单编号" width="80" align="center" prop="id" />
          <el-table-column label="创建者" align="center" prop="creator.name" :show-overflow-tooltip="true" />
          <el-table-column label="类型" align="center">
            <template slot-scope="scope">
              {{ dictFormat(leaveTypeOpts, scope.row.leaveType) }}
            </template>
          </el-table-column>
          <el-table-column label="时长" width="120" prop="duration" />
          <el-table-column label="开始时间" align="center" prop="startTime" width="180" />
          <el-table-column label="结束时间" align="center" prop="endTime" width="180" />
          <el-table-column label="原因" align="center" prop="reason" :show-overflow-tooltip="true" />
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
          @pagination="listForm"
        />
      </el-card>

      <el-dialog :title="title" :visible.sync="open" :close-on-click-modal="false" width="650px">
        <el-form ref="form" :model="form" :rules="rules" label-width="80px">
          <el-row>
            <el-col :span="12">
              <el-form-item label="假期类型" prop="leaveType">
                <el-select
                  v-model="form.leaveType"
                  placeholder="请选择假期类型"
                  clearable
                  style="width: 100%"
                >
                  <el-option
                    v-for="o in leaveTypeOpts"
                    :key="o.id"
                    :label="o.label"
                    :value="o.value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="请假时长" prop="duration">
                <el-input v-model="form.duration" :disabled="true" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="开始时间" prop="startTime">
                <el-date-picker
                  v-model="form.startTime"
                  type="datetime"
                  default-time="9:00:00"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  @change="onStartEndTimeChange"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="结束时间" prop="endTime">
                <el-date-picker
                  v-model="form.endTime"
                  type="datetime"
                  default-time="18:00:00"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  @change="onStartEndTimeChange"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="原因" prop="reason">
                <el-input v-model="form.reason" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="详细原因" prop="detialReason">
                <el-input v-model="form.detialReason" type="textarea" :autosize="{ minRows: 6, maxRows: 6}" />
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
import { userSelectQuery } from '@/api/sys/user'
import { listLeaveForms, createLeaveForm, updateLeaveForm, getLeaveForm } from '@/api/forms/leave'

export default {
  name: 'LeaveForm',
  data() {
    const validateStartEndTime = (rule, value, callback) => {
      const { startTime, endTime } = this.form
      if (startTime && endTime && (new Date(startTime).getTime() - new Date(endTime).getTime() > 0)) {
        return callback(new Error('结束时间不能早于开始时间'))
      }
      callback()
    }
    return {
      loading: false,
      userSelectLoading: false,
      leaveTypeOpts: [],
      userSelectOpts: [],
      single: true,
      multiple: true,
      ids: [],

      edit: false,
      open: false,
      title: undefined,

      queryParams: {
        page: 1,
        limit: 10,

        type: undefined,
        creatorId: undefined
      },
      records: [],
      total: 0,

      form: {},
      rules: {
        leaveType: [{
          required: true, message: '假期类型不能为空', trigger: 'change'
        }],
        startTime: [{
          required: true, message: '开始时间不能为空', trigger: ['blur', 'change']
        }, {
          validator: validateStartEndTime,
          trigger: ['blur', 'change']
        }],
        endTime: [{
          required: true, message: '结束时间不能为空', trigger: ['blur', 'change']
        }, {
          validator: validateStartEndTime,
          trigger: ['blur', 'change']
        }],
        reason: [{
          required: true, message: '原因不能为空', trigger: 'blur'
        }, {
          validator: (rule, value, callback) => {
            if (value && value.length > 100) {
              return callback(new Error('原因不能超过100个字符'))
            }
            callback()
          },
          trigger: 'blur'
        }],
        detailReason: [{
          validator: (rule, value, callback) => {
            if (value && value.length > 500) {
              return callback(new Error('详细原因不能超过500个字符'))
            }
            callback()
          },
          trigger: 'blur'
        }]
      }
    }
  },
  computed: {

  },
  created() {
    this.getDicts('sys_level_type').then(resp => {
      this.leaveTypeOpts = resp.data
    })
    this.selectUser('')
    this.listForm()
  },
  methods: {
    reset() {
      this.form = {
        type: undefined,
        startTime: undefined,
        endTime: undefined,
        duration: 0,
        reason: undefined
      }
      this.resetForm('form')
    },
    listForm() {
      listLeaveForms(this.queryParams).then(resp => {
        this.loading = false
        const { data } = resp
        this.records = data.records
        this.total = data.total
      })
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    handleQuery() {
      this.loading = true
      this.listForm()
    },
    handleAdd() {
      this.reset()
      this.title = '新建请假单'
      this.open = true
      this.edit = false
    },
    handleUpdate(row) {
      const id = row && row.id || this.ids[0]
      getLeaveForm(id).then(resp => {
        const { data } = resp
        this.from = data
        this.title = '修改请假单'
        this.open = true
        this.edit = true
      })
    },
    handleDelete(row) {

    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    cancel() {
      this.reset()
      this.open = false
    },
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (!valid) {
          return
        }
        const action = this.form.id ? (form) => updateLeaveForm(this.form.id, form) : createLeaveForm
        const msg = this.form.id ? '修改成功' : '新建成功'
        action(this.form).then(resp => {
          if (resp.success) {
            this.msgSuccess(msg)
            this.open = false
            this.listForm()
          } else {
            this.msgError(resp.message)
          }
        })
      })
    },
    selectUser(query) {
      this.userSelectLoading = true
      userSelectQuery(query).then(resp => {
        this.userSelectOpts = resp.data.records
        this.userSelectLoading = false
      })
    },
    caculateDuration(start, end) {
      return ((new Date(end).getTime() - new Date(start).getTime()) / (1000 * 60 * 60)).toFixed(1)
    },
    onStartEndTimeChange() {
      const { startTime, endTime } = this.form
      let duration = 0
      if (startTime && endTime) {
        duration = this.caculateDuration(startTime, endTime)
        duration = duration > 0 ? duration : 0
      }
      this.form.duration = duration
    }
  }

}
</script>

<style>

</style>
