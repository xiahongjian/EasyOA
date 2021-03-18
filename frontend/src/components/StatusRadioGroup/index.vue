<template>
  <el-radio-group v-bind="$attrs" v-on="$listeners">
    <el-radio
      v-for="s in statusOptions"
      :key="s.value"
      :label="s.value"
    >{{ s.label }}</el-radio>
  </el-radio-group>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
  name: 'StatusRadioGroup',
  computed: {
    ...mapGetters(['statusOptions'])
  }
}
</script>
