<template>
  <basic-layout>
    <template #wrapper>
      <el-card class="box-card">
        <el-form ref="queryForm" :inline="true" :model="queryParams" />
      </el-card>
    </template>
  </basic-layout>
</template>

<script>
export default {
  name: 'ProcessModel',
  data() {
    return {
      queryPrams: {
        key: undefined,
        name: undefined,
        page: 0,
        limit: 10
      }
    }
  },
  created() {

  },
  methods: {

  }
}
</script>

<style>

</style>
