<template>
  <span>
    <el-input
      :value="userNames"
      v-bind="$attrs"
      readonly
    >
      <el-button
        slot="append"
        icon="el-icon-search"
        @click="showWindow"
      >
        选择
      </el-button>
    </el-input>
    <el-dialog
      v-el-drag-dialog
      :title="title"
      :visible.sync="open"
      :close-on-click-modal="false"
      width="70%"
      @close="cancel"
    >
      <el-row :gutter="20" class="content-body">
        <el-col :span="5">
          <el-card shadow="never" body-style="height: 450px">
            <div slot="header"><span class="card-title">选择部门</span></div>
            <el-input
              v-model="filter"
              placeholder="输入部门名称"
            />
            <el-tree
              ref="deptTree"
              :data="deptOptions"
              :props="defaultProps"
              :expand-on-click-node="false"
              :filter-node-method="filterNode"
              default-expand-all
              style="height: 380px; overflow: auto;"
              @node-click="handleNodeClick"
            />
          </el-card>
        </el-col>
        <el-col :span="14">
          <el-card shadow="never" body-style="height: 450px; border:2px;">
            <div slot="header"><span>选择用户</span></div>
            <el-form ref="queryForm" :inline="true" :model="queryParams">
              <el-form-item label="关键字" prop="keyword">
                <el-input
                  v-model="queryParams.keyword"
                  placeholder="请输入姓名或邮箱"
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
              v-loading="userRecordLoading"
              :data="records"
              height="330px"
              @selection-change="handleSelectionChange"
            >
              <el-table-column
                label="选择"
                width="55"
                align="center"
              >
                <template slot-scope="scope">
                  <el-checkbox v-model="scope.row.checked" @change="(value) => handleSelectionChange(value, scope.row)" />
                </template>
              </el-table-column>
              <el-table-column label="姓名" prop="name" width="120" />
              <el-table-column label="用户名" prop="username" width="180" />
              <el-table-column label="邮箱" prop="email" show-overflow-tooltip />
              <el-table-column label="部门" prop="department" width="120" />
            </el-table>
            <pagination
              v-show="total > 0"
              :total="total"
              :page.sync="queryParams.page"
              :limit.sync="queryParams.limit"
              @pagination="handleQuery"
            />
          </el-card>
        </el-col>
        <el-col :span="5">
          <el-card shadow="never" body-style="height: 450px">
            <div slot="header">
              <span>已选中用户</span>
            </div>
            <el-table
              :data="selectedRecords"
              height="420px"
            >
              <el-table-column label="姓名" prop="name" />
              <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="100px">
                <template slot-scope="scope">
                  <el-button
                    size="mini"
                    icon="el-icon-delete"
                    type="text"
                    @click="deleteSelected(scope.index, scope.row)"
                  >删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>
      </el-row>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleDoSelect">确定</el-button>
        <el-button @click="cancel">取消</el-button>
      </div>
    </el-dialog>
  </span>

</template>

<script>
import { listUsers, getUserByIds } from '@/api/sys/user'
import { listDept } from '@/api/sys/dept'
export default {
  name: 'UserPicker',
  model: {
    prop: 'user',
    event: 'change'
  },
  props: {
    user: {
      type: Number || Array,
      default: undefined
    },
    multiSelect: {
      type: Boolean,
      default: false
    },
    title: {
      type: String,
      default: '选择用户'
    }
  },
  data() {
    return {
      originUser: undefined,
      orginUserSelection: undefined,

      queryParams: {
        dept: undefined,
        keyword: undefined,
        status: 1,
        limit: 10,
        page: 1
      },
      // 用于标识对话框是否显示
      open: false,

      deptOptions: [],
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      filter: undefined,

      // 标识用户查询是否在进行中
      userRecordLoading: true,
      // 用于存放用户查询结果
      records: [],
      total: 0,

      // 用于存放已选中的用户，提供给右侧已选中表格使用
      selectedRecords: []

    }
  },
  computed: {
    userNames() {
      return this.multiSelect
        ? this.selectedRecords.map(e => e.name).join(',')
        : (this.selectedRecords.length > 0
          ? this.selectedRecords[0].name
          : undefined
        )
    }
  },
  watch: {
    filter(value) {
      this.$refs.deptTree.filter(value)
    }
  },
  async created() {
    // 查找selectedValues中的用户信息，并存放valueSelection中
    this.initUserSelection()
  },
  methods: {
    selectedUser() {
      return this.multiSelect
        ? this.selectedRecords.map(e => e.id)
        : (this.selectedRecords.length > 0
          ? this.selectedRecords[0].id
          : undefined
        )
    },
    resetQuery() {
      this.resetForm('queryForm')
      this.queryParams.dept = undefined
      this.handleQuery()
    },
    async handleQuery() {
      this.userRecordLoading = true
      const { data } = await listUsers(this.queryParams)
      data.records.forEach(e => {
        this.selectedRecords.forEach(selected => {
          if (e.id === selected.id) {
            e.checked = true
          }
        })
      })
      this.records = data.records

      this.total = data.total
      this.userRecordLoading = false
    },
    showWindow() {
      this.open = true
      // 将原值保存下来
      this.originUser = this.multiSelect ? Object.assign([], this.user) : this.user
      this.orginUserSelection = Object.assign([], this.selectedRecords)
      this.handleQuery()
      this.initDeptSelect()
    },
    async initTableSelected() {
      this.records.forEach((e, index) => {
        this.selectedRecords.forEach(selected => {
          e.checked = selected.id === e.id
          this.records.splice(index, 0)
        })
      })
    },
    async initDeptSelect() {
      if (!this.isEmpty(this.deptOptions)) {
        return
      }
      // 获取部门选项
      const { data } = await listDept()
      const options = [{
        id: -1,
        name: '主类目',
        children: data
      }]
      this.deptOptions = options
    },
    // 获取绑定User对应的用户信息
    async initUserSelection() {
      if (!this.isEmpty(this.user)) {
        const { data } = await getUserByIds(this.user)
        this.selectedRecords = data
      }
    },
    handleSelectionChange(value, row) {
      // 处理单选的情况
      if (!this.multiSelect) {
        if (value) {
          this.selectedRecords = [row]
          // 将其他所有选中的记录checked设置为false
          this.records.forEach((e, index) => {
            if (e.id !== row.id) {
              e.checked = false
              this.records.splice(index, 0)
            }
          })
        } else {
          this.selectedRecords.splice(0, 1)
          this.records.forEach((e, index) => {
            if (e.checked === true) {
              e.checked = false
              this.records.splice(index, 0)
            }
          })
        }
      } else { // 处理多选
        // 选中
        if (value) {
          this.selectedRecords.push(row)
        } else { // 取消选中
          let index = -1
          for (let i = 0; i < this.selectedRecords; i++) {
            if (this.selectedRecords[i].id === row.id) {
              index = i
              break
            }
          }
          if (index !== -1) {
            this.selectedRecords.splice(index, 1)
          }
        }
      }
    },
    deleteSelected(index, row) {
      this.selectedRecords.splice(index, 1)
      this.records.forEach((e, index) => {
        if (e.id === row.id) {
          e.checked = false
          this.records.splice(index, 0) // 必须执行此行代码，否则表格数据不更新
        }
      })
    },
    handleDoSelect() {
      const selectedUser = this.selectedUser()
      // 触发change事件
      this.$emit('change', selectedUser, this.originUser)
      this.open = false
    },
    cancel() {
      this.selectedRecords = this.orginUserSelection
      this.open = false
    },
    filterNode(value, data) {
      if (!value) {
        return true
      }
      return data.name.indexOf(value) !== -1
    },
    handleNodeClick(data) {
      if (data.id === -1) {
        this.queryParams.dept = undefined
      } else {
        this.queryParams.dept = data.id
      }
      this.handleQuery()
    }
  }
}
</script>

<style scoped>
.contaner {
  height: 100%;
  overflow: auto;
}
.content-body {
  margin-top: -20px;
  height: 500px;
}

</style>
