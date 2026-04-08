<template>
  <div class="bill-container">
    <el-card class="bill-card">
      <template #header>
        <div class="card-header">
          <span>记账</span>
          <div class="header-actions">
            <el-button type="primary" @click="dialogVisible = true">添加账单</el-button>
            <el-date-picker
              v-model="searchForm.month"
              type="month"
              placeholder="选择月份"
              format="YYYY-MM"
              value-format="YYYY-MM"
              @change="handleSearch"
              style="margin-left: 10px;"
            />
            <el-select
              v-model="searchForm.category"
              placeholder="分类"
              @change="handleSearch"
              style="margin-left: 10px;"
            >
              <el-option label="全部" value="" />
              <el-option label="餐饮" value="餐饮" />
              <el-option label="交通" value="交通" />
              <el-option label="购物" value="购物" />
              <el-option label="娱乐" value="娱乐" />
              <el-option label="其他" value="其他" />
            </el-select>
          </div>
        </div>
      </template>
      
      <el-table :data="billList.records || []" style="width: 100%">
        <el-table-column prop="date" label="日期" width="120" />
        <el-table-column prop="type" label="类型" width="80">
          <template #default="scope">
            <span v-if="scope.row.type === 'income'" style="color: green">收入</span>
            <span v-else style="color: red">支出</span>
          </template>
        </el-table-column>
        <el-table-column prop="amount" label="金额" width="120">
          <template #default="scope">
            <span v-if="scope.row.type === 'income'" style="color: green">{{ scope.row.amount }}</span>
            <span v-else style="color: red">{{ scope.row.amount }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="category" label="分类" width="120" />
        <el-table-column prop="description" label="备注" />
      </el-table>
      
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pageForm.pageNum"
          v-model:page-size="pageForm.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="billList.total || 0"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
    
    <!-- 添加账单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      title="添加账单"
      width="500px"
    >
      <el-form :model="billForm" :rules="billRules" ref="billFormRef" label-width="100px">
        <el-form-item label="类型" prop="type">
          <el-radio-group v-model="billForm.type">
            <el-radio label="income">收入</el-radio>
            <el-radio label="expense">支出</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="金额" prop="amount">
          <el-input v-model.number="billForm.amount" placeholder="请输入金额" />
        </el-form-item>
        
        <el-form-item label="分类" prop="category">
          <el-select v-model="billForm.category" placeholder="请选择分类">
            <el-option label="餐饮" value="餐饮" />
            <el-option label="交通" value="交通" />
            <el-option label="购物" value="购物" />
            <el-option label="娱乐" value="娱乐" />
            <el-option label="工资" value="工资" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="日期" prop="date">
          <el-date-picker
            v-model="billForm.date"
            type="date"
            placeholder="选择日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        
        <el-form-item label="备注" prop="description">
          <el-input v-model="billForm.description" placeholder="请输入备注" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleAddBill" :loading="loading">提交</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../utils/request'

const billFormRef = ref(null)
const dialogVisible = ref(false)
const loading = ref(false)

const billForm = reactive({
  type: 'expense',
  amount: 0,
  category: '',
  date: new Date().toISOString().split('T')[0],
  description: ''
})

const searchForm = reactive({
  month: new Date().toISOString().substring(0, 7),
  category: ''
})

const pageForm = reactive({
  pageNum: 1,
  pageSize: 10
})

const billList = ref({
  records: [],
  total: 0
})

const billRules = {
  type: [
    { required: true, message: '请选择类型', trigger: 'change' }
  ],
  amount: [
    { required: true, message: '请输入金额', trigger: 'blur' },
    { type: 'number', message: '请输入正确的金额', trigger: 'blur' },
    { validator: (rule, value, callback) => {
        if (value <= 0) {
          callback(new Error('金额必须大于0'))
        } else {
          callback()
        }
      }, trigger: 'blur' }
  ],
  category: [
    { required: true, message: '请选择分类', trigger: 'change' }
  ],
  date: [
    { required: true, message: '请选择日期', trigger: 'change' }
  ]
}

// 获取账单列表
const getBillList = async () => {
  try {
    const response = await request.get('/bill/page', {
      params: {
        pageNum: pageForm.pageNum,
        pageSize: pageForm.pageSize,
        category: searchForm.category,
        month: searchForm.month
      }
    })
    billList.value = response
  } catch (error) {
    console.error('获取账单列表失败:', error)
  }
}

// 处理搜索
const handleSearch = () => {
  pageForm.pageNum = 1
  getBillList()
}

// 处理页码变化
const handleCurrentChange = (current) => {
  pageForm.pageNum = current
  getBillList()
}

// 处理每页大小变化
const handleSizeChange = (size) => {
  pageForm.pageSize = size
  pageForm.pageNum = 1
  getBillList()
}

// 添加账单
const handleAddBill = async () => {
  try {
    await billFormRef.value.validate()
    loading.value = true
    await request.post('/bill', billForm)
    ElMessage.success('添加成功')
    // 关闭对话框
    dialogVisible.value = false
    // 重置表单
    billFormRef.value.resetFields()
    billForm.date = new Date().toISOString().split('T')[0]
    // 重新获取账单列表
    getBillList()
  } catch (error) {
    console.error('添加账单失败:', error)
  } finally {
    loading.value = false
  }
}

// 页面挂载时获取账单列表
onMounted(() => {
  getBillList()
})
</script>

<style scoped>
.bill-container {
  padding: 0 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-actions {
  display: flex;
  align-items: center;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.bill-card {
  margin-bottom: 20px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
}
</style>
