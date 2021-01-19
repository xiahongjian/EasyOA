<template>
  <BasicLayout>
    <template #wrapper>
      <el-card class="box-card">
        <el-form ref="queryForm" :model="queryParams" :inline="true">
          <el-form-item label="字典名称" prop="key">
            <el-select v-model="queryParams.key" size="small">
              <el-option
                v-for="item in typeOptions"
                :key="item.id"
                :label="item.name"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="字典标签" prop="label">
            <el-input
              v-model="queryParams.label"
              placeholder="请输入字典标签"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="queryParams.status" placeholder="数据状态" clearable size="small">
              <el-option
                v-for="s in statusOptions"
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
import { listData } from '@/api/system/dict/data'
import { listType, getType } from '@/api/system/dict/type'
import BasicLayout from '@/layout/BasicLayout.vue'
export default {
  name: 'Data',
  components: { BasicLayout },
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      totoal: 0,
      records: [],
      defaultDictType: '',
      title: '',
      isEdit: false,
      typeOptions: [],

      queryParams: {
        page: 1,
        pageSize: 10,
        label: undefined,
        key: undefined,
        status: undefined
      },

      form: {},
      rules: {
        label: [
          { required: true, message: '数据标签不能为空', trigger: 'blur' }
        ],
        value: [
          { required: true, message: '数据键值不能为空', trigger: 'blur' }
        ],
        sort: [
          { required: true, message: '数据顺序不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    ...mapGetters(['statusOptions'])
  },
  created() {
    const dictId = this.$route.params && this.$route.params.dictId
    this.getType(dictId)
    this.getTypeList()
  },
  methods: {
    getType(dictId) {
      getType(dictId).then(resp => {
        const { data } = resp
        this.queryParams.key = data.key
        this.defaultDictType = data.key
        this.getList()
      })
    },
    getTypeList() {
      listType().then(resp => {
        this.typeOptions = resp.data.records
      })
    },
    getList() {
      this.loading = true
      listData(this.queryParams).then(resp => {
        const { data } = resp
        this.records = data.records
        this.totoal = data.totoal
        this.loading = false
      })
    },
    handleQuery() {

    },
    resetQuery() {

    }
  }
}
</script>
