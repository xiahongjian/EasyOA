<template>
  <basic-layout>
    <template #wrapper>
      <el-card class="box-card">
        <el-form ref="queryForm" :inline="true" :model="queryParams">
          <el-form-item label="流程名称" prop="name">
            <el-input
              v-model="queryParams.name"
              clearable
              size="small"
              style="width: 240px"
              placeholder="流程名称"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="发起人" prop="creator">
            <user-picker
              v-model="queryParams.creator"
              clearable
              size="small"
              style="width: 240px"
              placeholder="发起人"
            />
          </el-form-item>
          <el-form-item label="状态" prop="suspended">
            <dict-select v-model="queryParams.suspended" placeholder="状态" :options="statusOpts" size="small" tyle="width: 240px" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>

        <el-table
          ref="table"
          v-loading="loading"
          :data="records"
        >
          <el-table-column label="流程名称" prop="processDefinitionName" />
          <el-table-column label="实例ID" prop="id" />
          <el-table-column label="业务表单" prop="businessKey" />
          <el-table-column label="发起人" prop="startUserInfo.name" />

          <el-table-column label="开始时间" prop="startTime" width="200" align="center" />
          <el-table-column label="结束时间" prop="endDate" width="200" align="center" />
          <el-table-column label="操作" class-name="small-padding fixed-width" width="300" align="center">
            <template slot-scope="scope">
              <el-button
                :disabled="scope.row.suspended"
                size="mini"
                type="text"
                icon="el-icon-timer"
                @click="handleSuspend(scope.row)"
              >挂起</el-button>
              <action-group
                style="margin-left: 10px;"
                text="更多操作"
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

    </template>
  </basic-layout>
</template>

<script>
import UserPicker from '@/components/UserPicker'
import ActionGroup from '@/components/ActionGroup'
import DictSelect from '@/components/DictSelect'
import { listInstance } from '@/api/process/instance'
export default {
  name: 'Task',
  components: {
    UserPicker,
    ActionGroup,
    DictSelect
  },

  data() {
    return {
      queryParams: {
        procDefName: undefined,
        name: undefined,
        suspended: undefined,
        page: 0,
        limit: 10
      },

      loading: false,
      records: [],
      total: 0,

      imageDialogOpen: false,
      imageUrl: undefined,

      moreActionCfg: [{
        text: '详情',
        icon: 'el-icon-s-promotion',
        handler: this.handleMoreInfo
      }],
      statusOpts: [{
        id: 1,
        label: '挂起',
        value: 2
      }, {
        id: 2,
        label: '激活',
        value: 1
      }]
    }
  },
  created() {
    this.listTask()
  },

  methods: {
    async listTask() {
      this.loading = true
      const { data } = await listInstance(this.queryParams)
      this.loading = false
      this.records = data.records
      this.total = data.total
    },
    handleQuery() {
      this.listTask()
    },
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    }
  }
}
</script>

<style>

</style>
