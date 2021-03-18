<template>
  <el-select
    v-bind="$attrs"
    value-key="id"
    clearable
    filterable
    remote
    :default-first-option="true"
    :remote-method="selectUser"
    :loading="loading"
    v-on="$listeners"
  >
    <el-option
      v-for="o of options"
      :key="o.id"
      :label="o.name"
      :value="o"
    >
      <span>{{ o.name }} ({{ o.email }})</span>
    </el-option>
  </el-select>
</template>

<script>
import { userSelectQuery } from '@/api/sys/user'
export default {
  name: 'UserSelect',
  props: {
    autoLoad: {
      type: Boolean,
      default: false
    },
    pageSize: {
      type: Number,
      default: 10
    }
  },
  data() {
    return {
      loading: true,
      options: []
    }
  },
  created() {
    if (this.autoLoad) {
      this.selectUser('', this.pageSize)
    }
  },
  methods: {
    selectUser(query) {
      this.loading = true
      userSelectQuery(query, this.pageSize).then(resp => {
        this.options = resp.data.records
        this.loading = false
      })
    }
  }
}
</script>

<style>

</style>
