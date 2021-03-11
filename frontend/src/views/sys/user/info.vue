<template>
  <basic-layout>
    <template #wrapper>
      <el-row>
        <el-form :inline="true">
          <el-form-item>
            <el-button type="warning" size="mini" icon="el-icon-refresh-left" @click="handleCancel">取消</el-button>
            <el-button type="primary" size="mini" icon="el-icon-check" @click="handleSave">保存</el-button>
          </el-form-item>
        </el-form>
      </el-row>
      <el-col :span="8">
        <el-card>
          <el-avatar />
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-card>
          <el-tabs v-model="activeTab">
            <el-tab-pane label="账号信息" name="accountInfo">
              <el-form label-position="right">
                <el-form-item label="用户编号" class="display-field">
                  <span>{{ record.id }}</span>
                </el-form-item>
                <el-form-item label="账号" class="display-field">
                  <span>{{ record.username }}</span>
                </el-form-item>
                <el-form-item label="邮箱" class="display-field">
                  <span>{{ record.email }}</span>
                </el-form-item>
                <el-form-item label="状态" class="display-field">
                  <span><el-tag
                    :type="record.status === 0 ? 'danger' : 'success'"
                    disable-transitions
                  >{{ statusFormat(record.status) }}</el-tag></span>
                </el-form-item>
                <el-form-item label="角色" class="display-field">
                  <span><el-tag
                    v-for="role in record.roles"
                    :key="role"
                    disable-transitions
                    class="role-tag"
                  >{{ role }}</el-tag></span>
                </el-form-item>
                <el-form-item label="创建时间" class="display-field">
                  <span>{{ record.createTime }}</span>
                </el-form-item>
                <el-form-item label="更新时间" class="display-field">
                  <span>{{ record.updateTime }}</span>
                </el-form-item>
              </el-form>
            </el-tab-pane>
            <el-tab-pane label="用户信息" name="userInfo">
              <el-form label-position="right">
                <el-form-item label="姓名" class="display-field">
                  <span>{{ record.name }}</span>
                </el-form-item>
                <el-form-item label="性别" class="display-field">
                  <span>{{ dictDataFormat(genderOptions, record.gender) }}</span>
                </el-form-item>
                <el-form-item label="岗位" class="display-field">
                  <span>{{ dictDataFormat(postOptions, record.post) }}</span>
                </el-form-item>
                <el-form-item label="隶属部门" class="display-field">
                  <span>{{ record.department }}</span>
                </el-form-item>
              </el-form>
            </el-tab-pane>
          <!-- <el-tab-pane label="Account" name="account">
            <account :user="user" />
          </el-tab-pane> -->
          </el-tabs>
        </el-card>
      </el-col>

    </template>
  </basic-layout>
</template>
<script>
import { mapGetters } from 'vuex'
import { getUser } from '@/api/sys/user'
import { getDicts } from '@/api/sys/dict/data'
export default {
  name: 'UserInfo',
  data() {
    return {
      record: {},
      activeTab: 'accountInfo',
      genderOptions: [],
      postOptions: []
    }
  },
  computed: {
    ...mapGetters(['statusOptions'])
  },
  created() {
    const userId = this.$route.params && this.$route.params.id
    this.loadUserInfo(userId)
    getDicts('sys_gender').then(resp => {
      this.genderOptions = resp.data
    })
    getDicts('sys_post').then(resp => {
      this.postOptions = resp.data
    })
  },
  methods: {
    loadUserInfo(id) {
      getUser(id).then(resp => {
        const { data } = resp
        this.record = data
      })
    },
    statusFormat(status) {
      return this.selectDictLabel(this.statusOptions, status)
    },
    dictDataFormat(options, value) {
      let label = '-'
      options.forEach(opt => {
        if (opt.value === value) {
          label = opt.label
          return
        }
      })
      return label
    }
  }

}
</script>
<style>
.display-field label {
  width: 90px;
  color: #99a9bf;
}
.role-tag {
  margin-right: 10px;
}
</style>
