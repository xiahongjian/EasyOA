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
          <!-- <el-form-item label="任务名称" prop="name">
            <el-input
              v-model="queryParams.name"
              clearable
              size="small"
              style="width: 240px"
              placeholder="任务名称"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item> -->
          <!-- <el-form-item label="状态" prop="suspended">
            <dict-select v-model="queryParams.suspended" placeholder="状态" :options="statusOpts" size="small" tyle="width: 240px" />
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
          <el-table-column label="流程名称" prop="name" />
          <el-table-column label="任务名称" prop="name" :show-overflow-tooltip="true" />
          <el-table-column label="办理人" prop="assigneeUserInfo.name" />
          <el-table-column label="发起人" prop="submitterUserInfo.name" />
          <el-table-column label="描述" prop="description" :show-overflow-tooltip="true" />

          <el-table-column label="状态" prop="suspended" align="center" width="80">
            <template slot-scope="scope"><el-tag :type="scope.row.suspended ? 'danger' : 'primary'">{{ scope.row.suspended ? '挂起' : '激活' }}</el-tag></template>
          </el-table-column>
          <el-table-column label="开始时间" prop="createTime" width="200" align="center" />
          <el-table-column label="到期时间" prop="dueDate" width="200" align="center" />
          <el-table-column label="操作" class-name="small-padding fixed-width" width="300" align="center">
            <template slot-scope="scope">
              <el-button
                :disabled="scope.row.suspended"
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleApprove(scope.row)"
              >同意</el-button>
              <el-button
                :disabled="scope.row.suspended"
                size="mini"
                type="text"
                icon="el-icon-timer"
                @click="handleReject(scope.row)"
              >驳回</el-button>
              <!-- <el-button
                size="mini"
                type="text"
                icon="el-icon-s-promotion"
                @click="handleMoreInfo(scope.row)"
              >详情</el-button> -->
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
import ActionGroup from '@/components/ActionGroup'
import { listTask, completeTask/*, reassignTask*/ } from '@/api/process/task'
export default {
  name: 'Task',
  components: {
    ActionGroup
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
      }, {
        text: '查看图片',
        icon: 'el-icon-picture-outline',
        handler: this.showImage
      }, {
        text: '下载XML',
        icon: 'el-icon-document'
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
      const { data } = await listTask(this.queryParams)
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
    showImage(record) {
      this.imageUrl = `processes/definitions/${encodeURIComponent(record.processDefinitionId)}/image`
      this.imageDialogOpen = true
    },
    closeImage() {
      this.imageDialogOpen = false
    },
    async handleApprove(record) {
      const resp = await completeTask(record.id, 'approve')
      if (resp.success) {
        this.msgSuccess('审批成功')
        this.listTask()
      }
    },
    async handleReject(record) {
      const resp = await completeTask(record.id, 'reject', 'Reject for testing')
      if (resp.success) {
        this.msgSuccess('驳回成功')
        this.listTask()
      }
    },
    handleMoreInfo() {

    }
  }
}
</script>

<style>

</style>
