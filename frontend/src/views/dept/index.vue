<template>
  <basic-layout>
    <template #wrapper>
      <el-card class="box-card">
        <el-form :inline="true">
          <el-form-item label="部门名称">
            <el-input
              v-model="queryParams.name"
              placeholder="请输入部门名称"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="queryParams.status" placeholder="部门状态" clearable ize="small">
              <el-option
                v-for="s in statusOptions"
                :key="s.value"
                :label="s.label"
                :value="s.value"
              />
            </el-select>
          </el-form-item>

          <!-- 按钮 -->
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button v-permisaction="['sys:dept:create']" type="primary" icon="el-icon-plus" size="mini" @click="handleCreate">新增</el-button>
          </el-form-item>
        </el-form>

        <!-- Table -->
        <el-table
          v-loading="loading"
          :data="records"
          row-key="id"
          default-expand-all
          :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
        >
          <el-table-column prop="name" label="部门名称" />
          <el-table-column prop="sort" label="排序" />
          <el-table-column prop="”status“" label="状态" width="100">
            <template slot-scope="scope">
              <el-tag
                :type="scope.row.status === '1' ? 'danger' : 'success'"
                disable-transitions
              >{{ statusFormat(scope.row) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="创建时间" align="center" prop="createTime" width="200" />
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button
                v-permisaction="['sys:dept:update']"
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
              >修改</el-button>
              <el-button
                v-permisaction="['sys:dept:create']"
                size="mini"
                type="text"
                icon="el-icon-plus"
                @click="handleCreate(scope.row)"
              >新增</el-button>
              <el-button
                v-if="scope.row.p_id != 0"
                v-permisaction="['sys:dept:delete']"
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
              >删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>

      <!-- 添加或更新部门 -->
      <el-dialog :title="title" :visible.sync="open" :close-on-click-modal="false" width="600px">
        <el-form ref="form" :model="form" :rules="rules" label-width="80px">
          <el-row>
            <el-col :span="24">
              <el-form-item label="上级部门" prop="parentId">
                <treeselect
                  v-model="form.parentId"
                  :options="deptOptions"
                  :normalizer="normalizer"
                  :show-count="true"
                  placeholder="选择上级部门"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="部门名称" prop="name">
                <el-input v-model="form.name" placeholder="请输入部门名称" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="显示排序" prop="orderNum">
                <el-input-number v-model="form.sort" controls-position="right" :min="0" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="负责人" prop="leaderId">
                <el-select
                  v-model="form.selectedUser"
                  placeholder="请输入负责人姓名"
                  clearable
                  filterable
                  remote
                  :remote-method="selectUser"
                  :loading="userSelectLoading"
                  @clear="userClear"
                  @change="userChange"
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
            </el-col>
            <el-col :span="12">
              <el-form-item label="联系电话" prop="phone">
                <el-input v-model="form.phone" maxlength="11" :disabled="true" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="邮箱" prop="email">
                <el-input v-model="form.email" maxlength="50" :disabled="true" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="部门状态">
                <el-radio-group v-model="form.status">
                  <el-radio
                    v-for="o in statusOptions"
                    :key="o.value"
                    :label="o.value"
                  >{{ o.label }}</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </el-dialog>
    </template>
  </basic-layout>
</template>

<script>
import { mapGetters } from 'vuex'
import { userSelectQuery } from '@/api/user'
import { listDept, getDept, createDept, updateDept/*, deleteDept*/ } from '@/api/dept'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
export default {
  name: 'Dept',
  components: { Treeselect },
  data() {
    return {
      loading: true,
      records: [],
      deptOptions: [],
      userSelectLoading: true,
      userSelectOpts: [],

      title: '',
      isEdit: false,
      open: false,

      queryParams: {
        name: undefined,
        status: undefined
      },

      form: {},
      rules: {
        parentId: [{
          required: true, message: '上级部门不能为空', trigger: 'blur'
        }, {
          validator: (rule, value, callback) => {
            if (this.form.id === value) {
              callback(new Error('不能以自己作为父级部门'))
            }
            callback()
          },
          trigger: 'blur'
        }],
        name: [{
          required: true, message: '不猛名称不能为空', trigger: 'blur'
        }],
        sort: [{
          required: true, message: '排序值不能为空', trigger: 'blur'
        }]
      }
    }
  },
  computed: {
    ...mapGetters(['statusOptions'])
  },
  created() {
    this.listDept()
  },
  methods: {
    listDept() {
      this.loading = true
      listDept(this.form).then(resp => {
        this.records = resp.data
        this.loading = false
      })
    },
    handleQuery() {
      listDept(this.queryParams)
    },
    handleCreate() {
      this.reset()
      this.getTreeselect()
      this.open = true
      this.title = '添加部门'
      this.isEdit = false
    },
    handleUpdate(row) {
      const id = row.id
      getDept(id).then(resp => {

      })
    },
    handleDelete() {

    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid) {
          return
        }
        const action = this.form.id ? (form) => updateDept(this.form.id, form) : createDept
        const msg = this.form.id ? '修改成功' : '新增成功'
        action(this.form).then(resp => {
          if (resp.success) {
            this.msgSuccess(msg)
            this.open = false
            this.listDept()
          } else {
            this.msgErrot(resp.message)
          }
        })
      })
    },
    reset() {
      this.form = {
        id: undefined,
        parentId: undefined,
        name: undefined,
        sort: 0,
        leaderId: undefined,
        mobile: undefined,
        email: undefined,
        status: 1
      }
    },
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status)
    },
    cancel() {
      this.open = false
      this.reset()
    },
    selectUser(query) {
      this.userSelectLoading = true
      userSelectQuery(query).then(resp => {
        this.userSelectOpts = resp.data.records
        this.userSelectLoading = false
      })
    },
    getTreeselect() {
      listDept().then(resp => {
        this.deptOptions = []
        const dept = {
          id: -1,
          name: '主类目',
          children: resp.data.records
        }
        this.deptOptions.push(dept)
      })
    },
    normalizer(node) {
      if (node.children && node.children.length === 0) {
        delete node.children
      }
      return {
        id: node.id,
        label: node.name,
        children: node.children
      }
    },
    userChange(row) {
      this.form.leaderId = row.id
      this.form.email = row.email
      this.form.mobile = row.mobile
    },
    userClear() {
      this.form.leaderId = undefined
      this.form.email = undefined
      this.form.mobile = undefined
    }
  }
}
</script>
