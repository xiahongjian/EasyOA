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
          </el-form-item>
        </el-form>

        <!-- 数据表格 -->

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
    // 字典状态字典翻译
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status)
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    }
  }

}
</script>
