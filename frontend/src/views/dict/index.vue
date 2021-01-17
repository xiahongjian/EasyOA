<template>
  <BasicLayout>
    <template #wrapper>
      <el-card class="box-card">
        <el-form ref="queryForm" :model="queryParams" :inline="true" label-width="68px">
          <el-form-item label="字典名称" prop="name">
            <el-input
              v-model="queryParams.name"
              placeholder="请输入字典名称"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="字典类型" prop="key">
            <el-input
              v-model="queryParams.key"
              placeholder="请输入字典类型"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select
              v-model="queryParams.status"
              placeholder="字典状态"
              clearable
              size="small"
              style="width: 240px"
            >
              <el-option
                v-for="s in itemStatus"
                :key="s.id"
                :label="s.label"
                :value="s.value"
              />
            </el-select>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </template>
  </BasicLayout>
</template>
<script>
import { mapGetters } from 'vuex'
import { listType } from '@/api/system/dict/type'
export default {
  name: 'Dict',
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      total: 0,
      records: [],
      title: '',
      isEdit: false,
      // 是否显示弹出层
      open: false,

      queryParams: {
        start: 0,
        limit: 10,
        name: undefined,
        key: undefined,
        status: undefined
      },
      form: {},
      roles: {
        name: [
          { required: true, message: '字典名称不能为空', trigger: 'blur' }
        ],
        key: [
          { required: true, message: '字典类型不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    ...mapGetters(['itemStatus'])
  },
  created() {

  },
  methods: {
    getList() {
      this.loading = true
      listType(this.queryParams).then(resp => {
        const { data } = resp
        this.records = data.records
        this.total = data.total
        this.loading = false
      })
    },
    handleQuery() {
      this.queryParams.start = 0
      this.getList()
    }
  }
}
</script>
