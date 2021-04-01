<template>
  <basic-layout>
    <template #wrapper>
      <el-card class="box-card">
        <el-form ref="queryForm" :inline="true" :model="queryParams">
          <el-form-item label="Key" prop="key">
            <el-input
              v-model="queryParams.modelId"
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

        <el-table
          ref="table"
          v-loading="loading"
          :data="records"
        >
          <!-- <el-table-column label="ID" prop="id" width="100" /> -->
          <el-table-column label="Key" prop="key" width="150" />
          <el-table-column label="名称" prop="name" :show-overflow-tooltip="true" />
          <el-table-column label="描述" prop="description" :show-overflow-tooltip="true" />
          <el-table-column label="版本" prop="version" align="center" />
          <el-table-column label="状态" prop="isSuspended" align="center" width="80">
            <template slot-scope="scope"><el-tag :type="scope.row.isSuspended ? 'danger' : 'primary'">{{ scope.row.isSuspended ? '挂起' : '正常' }}</el-tag></template>
          </el-table-column>
          <el-table-column label="操作" class-name="small-padding fixed-width" width="300" align="center">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
              >删除</el-button>
              <el-button
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
import { listProcDef } from '@/api/process/definition'
import ActionGroup from '@/components/ActionGroup'
export default {
  name: 'ProcDef',
  components: {
    ActionGroup
  },

  data() {
    return {
      queryParams: {
        key: undefined,
        name: undefined,
        page: 0,
        limit: 10
      },

      loading: false,
      records: [],
      total: 0,

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
      }]
    }
  },
  created() {
    this.listProcDef()
  },

  methods: {
    async listProcDef() {
      this.loading = true
      const { data } = await listProcDef(this.queryParams)
      this.loading = false
      this.records = data.records
      this.total = data.total
    },
    handleQuery() {
      this.listProcDef()
    },
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    showImage(record) {
      this.imageUrl = `processes/definitions/${encodeURIComponent(record.id)}/image`
      this.imageDialogOpen = true
    },
    closeImage() {
      this.imageDialogOpen = false
    },
    downloadXML(record) {
      const elink = document.createElement('a')
      elink.style.display = 'none'
      elink.target = '_blank'
      elink.href = `/processes/definitions/${encodeURIComponent(record.id)}/xml`
      document.body.appendChild(elink)
      elink.click()
      URL.revokeObjectURL(elink.href) // 释放URL 对象
      document.body.removeChild(elink)
    }
  }
}
</script>

<style>

</style>
