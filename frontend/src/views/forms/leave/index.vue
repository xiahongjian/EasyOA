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
          <el-form-item label="创建者" prop="requester">
            <el-select
              v-model="queryParams.requester"
              value-key="id"
              placeholder="请选择创建者"
              clearable
              filterable
              remote
              :default-first-option="true"
              :remote-method="selectUser"
              :loading="userSelectLoading"
            >
              <el-option
                v-for="o of userSelectOpts"
                :key="o.id"
                :label="o.name"
                :value="o"
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
              v-permisaction="['form:leaveform:add']"
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
          <el-table-column label="创建者" align="center" prop="requesterName" :show-overflow-tooltip="true" />
          <el-table-column label="类型" align="center">
            <template slot-scope="scope">
              {{ dictFormat(leaveTypeOpts, scope.row.type) }}
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
    </template>
  </basic-layout>
</template>

<script>
import { getDicts } from '@/api/sys/dict/data'
import { userSelectQuery } from '@/api/sys/user'
import { listLeaveForms } from '@/api/forms/leave'

export default {
  name: 'LeaveForm',
  data() {
    return {
      loading: false,
      userSelectLoading: false,
      leaveTypeOpts: [],
      userSelectOpts: [],
      single: true,
      multiple: true,

      queryParams: {
        page: 1,
        limit: 10,

        type: undefined
      },
      records: [],
      total: 0,

      form: {}
    }
  },
  computed: {

  },
  created() {
    getDicts('sys_level_type').then(resp => {
      this.leaveTypeOpts = resp.data
    })
    this.selectUser('')
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
        const { data } = { resp }
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
      this.listForm(this.queryParams)
    },
    handleAdd() {

    },
    handleUpdate(row) {

    },
    handleDelete(row) {

    },
    handleSelectionChange() {

    },
    dictFormat(opts, value) {
      let label = '-'
      opts.forEach(opt => {
        if (opt.value === value) {
          label = opt.label
          return
        }
      })
      return label
    },
    selectUser(query) {
      this.userSelectLoading = true
      userSelectQuery(query).then(resp => {
        this.userSelectOpts = resp.data.records
        this.userSelectLoading = false
      })
    }
  }

}
</script>

<style>

</style>
