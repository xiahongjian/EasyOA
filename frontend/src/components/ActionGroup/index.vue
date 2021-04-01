<template>
  <el-dropdown v-bind="$attrs" @command="handleMoreAction">
    <el-button :type="type" :size="size">
      {{ text }}<i class="el-icon-arrow-down el-icon--right" />
    </el-button>
    <el-dropdown-menu slot="dropdown">
      <el-dropdown-item
        v-for="(action, index) in actions"
        :key="index"
        :command="buildCommand(action.handler, data)"
        :icon="action.icon || ''"
      >{{ action.text }}</el-dropdown-item>
    </el-dropdown-menu>
  </el-dropdown>
</template>

<script>
export default {
  name: 'ButtonGroup',
  props: {
    text: {
      type: String,
      default: '更多'
    },
    type: {
      type: String,
      default: 'default'
    },
    size: {
      type: String,
      default: ''
    },
    data: {
      type: Object,
      default() {
        return {}
      }
    },
    actions: {
      type: Array,
      default() {
        return []
      }
    }
  },
  data() {
    return {
    }
  },
  methods: {
    buildCommand(handler, data) {
      return {
        handler,
        data
      }
    },
    handleMoreAction(command) {
      return command.handler(command.data)
    }
  }
}
</script>
