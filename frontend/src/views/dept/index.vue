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
          :tree-props="{children, hasChildren}"
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
    </template>
  </basic-layout>
</template>

<script>
import { mapGetters } from 'vuex'
import { listDept/*, getDept, createDept, updateDept, deleteDept*/ } from '@/api/dept'
// import Treeselect from '@riophae/vue-treeselect'
// import '@riophae/vue-treeselect/dist/vue-treeselect.css'
export default {
  name: 'Dept',
  // components: { Treeselect },
  data() {
    return {
      loading: true,
      records: [],
      deptOptions: [],

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
    ...mapGetters(['visibleOptions', 'menuTypeOptions'])
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

    },
    handleCreate() {

    },
    handleUpdate() {

    },
    handleDelete() {

    },
    submitForm() {

    },
    reset() {

    },
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status)
    }
  }
}
</script>
