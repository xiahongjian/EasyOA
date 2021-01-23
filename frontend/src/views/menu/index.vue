<template>
  <basic-layout>
    <template #wrapper>
      <el-card class="box-card">

        <!-- query表单 -->
        <el-form ref="queryForm" :model="queryParams" :inline="true" label-width="68px">
          <el-form-item label="菜单名称" prop="query">
            <el-input
              v-model="queryParams.query"
              placeholder="请输入菜单名称"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="状态" prop="visible">
            <el-select
              v-model="queryParams.visible"
              placeholder="状态"
              clearable
              size="small"
              style="width: 240px"
            >
              <el-option
                v-for="s in visibleOptions"
                :key="s.id"
                :label="s.label"
                :value="s.value"
              />
            </el-select>
          </el-form-item>

          <!-- 按钮 -->
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            <el-button
              v-permisaction="['sys:menu:create']"
              type="primary"
              icon="el-icon-plus"
              size="mini"
              @click="handleCreate"
            >新增</el-button>
          </el-form-item>
        </el-form>

        <!-- 数据表格 -->
        <el-table
          v-loading="loading"
          :data="records"
          row-key="id"
          :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
        >
          <el-table-column prop="title" label="菜单名称" :show-overflow-tooltip="true" width="220px" />
          <el-table-column prop="icon" label="图标" align="center" width="100px">
            <template slot-scope="scope">
              <svg-icon :icon-class="scope.row.icon" />
            </template>
          </el-table-column>
          <el-table-column prop="sort" label="排序" align="center" width="60px" />
          <el-table-column prop="permission" label="权限标识" :show-overflow-tooltip="true" />
          <el-table-column prop="routePath" label="路径" :show-overflow-tooltip="true">
            <template slot-scope="scope">
              <span v-if="scope.row.type == 4">{{ scope.row.routePath }}</span>
              <span v-else>{{ scope.row.component }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="visible" label="可见" :formatter="visibleFormat" width="80">
            <template slot-scope="scope">
              <el-tag
                :type="scope.row.visible === false ? 'danger' : 'success'"
                disable-transitions
              >{{ visibleFormat(scope.row) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="创建时间" align="center" prop="createTime" width="180" />
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="220">
            <template slot-scope="scope">
              <el-button
                v-permisaction="['sys:menu:update']"
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
              >修改</el-button>
              <el-button
                v-permisaction="['sys:menu:create']"
                size="mini"
                type="text"
                icon="el-icon-plus"
                @click="handleCreate(scope.row)"
              >新增</el-button>
              <el-button
                v-permisaction="['sys:menu:delete']"
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
              >删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </template>
  </basic-layout>
</template>
<script>
import { mapGetters } from 'vuex'
import { listMenu/*, createMenu, updateMenu, deleteMenu, getMenu*/ } from '@/api/menu'
export default {
  name: 'Menu',
  data() {
    return {
      loading: false,
      records: [],
      title: '',
      isEdit: false,
      open: false,

      queryParams: {
        query: undefined,
        status: undefined
      },
      form: {},
      rules: {

      }
    }
  },
  computed: {
    ...mapGetters(['visibleOptions'])
  },
  created() {
    this.listMenu()
  },
  methods: {
    listMenu() {
      this.loading = true
      listMenu(this.queryParams).then(resp => {
        const { data } = resp
        this.records = data
        this.loading = false
      })
    },
    handleQuery() {
      this.listMenu()
    },

    handleCreate() {

    },

    handleUpdate() {

    },

    handleDelete() {

    },

    visibleFormat(row) {
      // 如果是按钮则显示 ‘-- --’
      if (row.type === 3) {
        return '-- --'
      }
      return this.selectDictLabel(this.visibleOptions, row.visible)
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    }
  }

}
</script>
