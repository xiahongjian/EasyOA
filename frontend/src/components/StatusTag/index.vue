<template>
  <el-tag
    :type="status === 0 ? 'danger' : 'success'"
    disable-transitions
    v-bind="$attrs"
  >{{ selectDictLabel(statusOptions, status) }}</el-tag>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
  name: 'StatusTag',
  props: {
    status: {
      type: Number,
      required: true
    }
  },
  computed: {
    ...mapGetters(['statusOptions'])
  }
}
</script>
