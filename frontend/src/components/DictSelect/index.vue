<template>
  <el-select
    clearable
    v-bind="$attrs"
    v-on="$listeners"
  >
    <el-option
      v-for="o in opts"
      :key="o.id"
      :label="o.label"
      :value="o.value"
    />
  </el-select>
</template>

<script>
export default {
  name: 'DictSelect',
  props: {
    dictType: {
      type: String,
      default: ''
    },
    options: {
      type: Array,
      default() {
        return []
      }
    }
  },
  data() {
    return {
      opts: []
    }
  },
  created() {
    if (this.dictType !== '') {
      this.getDicts(this.dictType).then(resp => {
        this.opts = resp.data
      })
    } else {
      this.opts = this.options
    }
  }
}
</script>
