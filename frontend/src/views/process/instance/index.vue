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
          <!-- <el-form-item label="状态" prop="state">
            <dict-select v-model="queryParams.state" placeholder="状态" :options="stateOpts" size="small" tyle="width: 240px" />
          </el-form-item> -->
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
          <el-table-column label="流程名称" prop="processDefinitionName" :show-overflow-tooltip="true" />
          <el-table-column label="实例ID" prop="id" :show-overflow-tooltip="true" />
          <el-table-column label="业务表单" prop="businessKey" />
          <el-table-column label="发起人" prop="startUserInfo.name" />
          <el-table-column label="当前任务" prop="currentTaskName" :show-overflow-tooltip="true" />
          <el-table-column label="办理人" prop="currentAssigneeUserInfo.name" />

          <el-table-column label="开始时间" prop="startTime" width="180" align="center" />
          <el-table-column label="结束时间" prop="endTime" width="180" align="center" />
          <el-table-column label="耗时" prop="durationStr" width="180" align="center" />
          <el-table-column label="状态" prop="state" align="center">
            <template slot-scope="scope">
              <el-tag :type="tagType(scope.row.state)">{{ selectDictLabel(stateOpts, scope.row.state) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" class-name="small-padding fixed-width" width="150" align="center">
            <template slot-scope="scope">
              <el-button
                v-show="scope.row.state !== 3"
                :disabled="scope.row.state === 2"
                size="mini"
                type="text"
                icon="el-icon-timer"
                @click="handleSuspend(scope.row)"
              >挂起</el-button>
              <el-button
                v-show="scope.row.state === 3"
                size="mini"
                type="text"
                icon="el-icon-s-promotion"
                @click="handleActive(scope.row)"
              >激活</el-button>
              <action-group
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
// import DictSelect from '@/components/DictSelect'
import { listInstance } from '@/api/process/instance'
export default {
  name: 'Task',
  components: {
    UserPicker,
    ActionGroup
    // DictSelect
  },

  data() {
    return {
      queryParams: {
        procDefName: undefined,
        name: undefined,
        suspended: undefined,
        page: 1,
        limit: 10
      },

      loading: false,
      records: [],
      total: 0,

      imageDialogOpen: false,
      imageUrl: undefined,

      moreActionCfg: [{
        text: '关闭',
        icon: 'el-icon-close',
        show(record) {
          return record.state !== 2
        }
      }, {
        text: '委派',
        icon: 'el-icon-s-promotion',
        handler: this.handleMoreInfo,
        show(record) {
          return record.state === 1
        }
      }, {
        text: '跳转',
        icon: 'el-icon-location-outline',
        show(record) {
          return record.state === 1
        }
      }, {
        text: '历史',
        icon: 'el-icon-tickets'
      }],
      stateOpts: [{
        id: 1,
        label: '启动',
        value: 1
      }, {
        id: 2,
        label: '完成',
        value: 2
      }, {
        id: 3,
        label: '挂起',
        value: 3
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
    },
    tagType(state) {
      return state === 3 ? 'warning' : (state === 1 ? 'success' : 'default')
    }
  }
}
</script>

<style>

</style>
