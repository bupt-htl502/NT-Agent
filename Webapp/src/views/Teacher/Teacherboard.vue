<template>
  <div class="teacher-dashboard">
    <div class="header">
      <div class="header-left">
        <el-tooltip class="box-item" effect="dark" content="返回主页" placement="right">
          <div class="home-icon" @click="handleHomeClick">
            <el-icon style="color: #409EFF;" size="24">
              <HomeFilled />
            </el-icon>
          </div>
        </el-tooltip>
        <h2>教师端</h2>
      </div>
      <div class="header-actions">
        <el-input
          v-model="searchKeyword"
          placeholder="请输入学生姓名或学号"
          style="width: 300px; margin-right: 12px;"
          @keyup.enter="handleSearch"
        >
          <template #append>
            <el-button @click="handleSearch">
              <el-icon><Search /></el-icon>
            </el-button>
          </template>
        </el-input>
        <el-button type="primary" @click="refreshData" icon="Refresh">刷新数据</el-button>
      </div>
    </div>

    <div class="filter-info" v-if="isFiltered">
      <span>查询结果：找到 {{ filteredData.length }} 条记录</span>
      <el-button type="text" @click="clearSearch">清除筛选</el-button>
    </div>

    <div class="table-container">
      <div class="simple-title">
        <h3>学生成绩</h3>
        <el-button class="teacher-download-student-info" @click="downloadStudentInfo">下载成绩单</el-button>
      </div>
      <el-table
        :data="paginatedData"
        stripe
        border
        style="width: 100%"
        v-loading="loading"
        element-loading-text="数据加载中..."
      >
        <el-table-column
          prop="name"
          label="学生姓名"
          width="200"
          align="center"
          fixed
        >
          <template #default="scope">
            <span class="student-name">{{ scope.row.name }}</span>
          </template>
        </el-table-column>

        <el-table-column
          prop="studentNo"
          label="学号"
          width="243"
          align="center"
        >
          <template #default="scope">
            <el-tag 
              type="info" 
              style="
                font-size: 16px;
                font-weight: bold;
                padding: 8px 12px;
                height: auto;
              "
            >
              {{ scope.row.studentNo }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column
          prop="averageScore"
          label="平均成绩"
          width="180"
          align="center"
          sortable
        >
          <template #default="scope">
            <el-tag
              :type="getScoreType(scope.row.averageScore)"
              effect="dark"
              style="
                font-size: 14px;
                font-weight: bold;
                padding: 8px 10px;
                height: auto;
              "
            >
              {{ scope.row.averageScore }} 分
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column
          prop="sumCommitTimes"
          label="提交次数"
          width="1200"
          align="center"
          sortable
        >
          <template #default="scope">
            <el-progress
              :percentage="getSubmissionPercentage(scope.row.sumCommitTimes)"
              :color="getSubmissionColor(scope.row.sumCommitTimes)"
              :show-text="false"
            />
            <span class="submission-count">{{ scope.row.sumCommitTimes }} 次</span>
          </template>
        </el-table-column>

        <el-table-column
          label="操作"
          width="200"
          align="center"
          fixed="right"
        >
          <template #default="scope">
            <el-button
              size="small"
              type="primary"
              @click="handleViewDetail(scope.row)"
            >
              查看详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container" v-if="displayData.length > 0">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="displayData.length"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>

    <div class="charts-container">
      <div class="simple-title">
        <h3>场景概况</h3>
      </div>
      
      <div class="charts-row">
        <!-- 平均成绩折线图 -->
        <div class="chart-card">
          <div class="chart-header">
            <span class="chart-title">平均成绩趋势</span>
            <el-select
              v-model="selectedChapterForScore"
              placeholder="选择场景"
              size="small"
              style="width: 200px;"
            >
              <el-option label="全部场景" value="" />
              <el-option v-for="chapter in uniqueChapters" :key="chapter" :label="chapter" :value="chapter" />
            </el-select>
          </div>
          <div class="chart-content">
            <div ref="scoreChartRef" style="width: 100%; height: 450px;"></div>
          </div>
        </div>

        <!-- 平均提交次数折线图 -->
        <div class="chart-card">
          <div class="chart-header">
            <span class="chart-title">平均提交次数趋势</span>
            <el-select
              v-model="selectedChapterForCommit"
              placeholder="选择场景"
              size="small"
              style="width: 200px;"
            >
              <el-option label="全部场景" value="" />
              <el-option v-for="chapter in uniqueChapters" :key="chapter" :label="chapter" :value="chapter" />
            </el-select>
          </div>
          <div class="chart-content">
            <div ref="commitChartRef" style="width: 100%; height: 450px;"></div>
          </div>
        </div>
      </div>
    </div>

    <!-- 学生详情弹窗 -->
    <el-dialog
      v-model="detailDialogVisible"
      :title="`${selectedStudent?.name} - 实验成绩详情`"
      width="60%"
    >
      <el-table
        :data="detailTableData"
        stripe
        border
      >
        <el-table-column
          prop="sceneName"
          label="实验场景"
          width="740"
          align="center"
        >
          <template #default="scope">
            <span class="scene-name">{{ scope.row.sceneName }}</span>
          </template>
        </el-table-column>

        <el-table-column
          prop="commitTimes"
          label="成绩"
          width="243"
          align="center"
        >
          <template #default="scope">
            <el-tag
              :type="getScoreType(scope.row.score)"
              effect="dark"
            >
              {{ scope.row.score }} 分
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column
          prop="commitTimes"
          label="提交次数"
          width="243"
          align="center"
        >
          <template #default="scope">
            <el-tag
              :type="getScoreType(scope.row.commitTimes)"
              effect="dark"
            >
              {{ scope.row.commitTimes }} 次
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed, watch, nextTick, onUnmounted } from 'vue'
import { ElMessage } from 'element-plus'
import { HomeFilled, Search } from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import { useRouter } from 'vue-router'
import { getScoreApi } from '@/apis/GradescoreApi'

const router = useRouter()

interface StudentRecord {
  studentList: StudentInfo[]
  statistics: StatisticsInfo
  sceneAverages: SceneAverage[]
}

interface StudentInfo {
  name: string
  studentNo: string
  averageScore: number
  sumCommitTimes: number
  scores: {
    [sceneName: string]: number
  }
  commitTimes: {
    [sceneName: string]: number
  }
}

interface StatisticsInfo {
  maxCommitTimes: number
}

interface SceneAverage {
  chapterName: string
  sceneName: string
  averageScore: number
  averageCommitTimes: number
}

interface DetailTableItem {
  sceneName: string
  score: number
  commitTimes: number
}

const studentData = ref<StudentRecord>({
  studentList: [],
  statistics: { maxCommitTimes: 10 },
  sceneAverages: []
})
const loading = ref(false)
const detailDialogVisible = ref(false)
const selectedStudent = ref<StudentInfo | null>(null)
const searchKeyword = ref('')
const isFiltered = ref(false)
const filteredData = ref<StudentInfo[]>([])

const scoreChartRef = ref<HTMLElement>()
const commitChartRef = ref<HTMLElement>()
let scoreChart: echarts.ECharts | null = null
let commitChart: echarts.ECharts | null = null
const selectedChapterForScore = ref('')
const selectedChapterForCommit = ref('')

const currentPage = ref(1)
const pageSize = ref(10)

const getCookie = (key: string): string | null => {
  const cookieArr = document.cookie.split('; ');
  for (const cookie of cookieArr) {
    const [name, value] = cookie.split('=');
    if (name === key) {
      return decodeURIComponent(value);
    }
  }
  return null;
};

const handleHomeClick = () => {
  router.push('/home')
}

const isLogin = (): boolean => {
  const studentName = getCookie('studentName');
  const studentId = getCookie('studentId');
  const studentNo = getCookie('studentNo');
  return !!studentName && !!studentId&& !! studentNo;
};

// 自动触发登录的函数
const autoLogin = () => {
  // 如果未登录，则跳转到后端登录接口
  if (!isLogin()) {
    window.location.href = 'http://10.101.170.78:5173/login';
  }
};

onMounted(() => {
  autoLogin();
});

const downloadStudentInfo = async () => {
  const studentName = getCookie('studentName')
  const studentNo = getCookie('studentNo')
  if (!studentName || !studentNo) {
            alert('用户信息缺失，请重新登录！');
            return;
        }

  const response = await fetch(`/api/transcript/getScript?studentName=${studentName}&studentNo=${studentNo}`)
  const blob = await response.blob();
  const url = URL.createObjectURL(blob);
  const link = document.createElement('a');
  link.href = url;
  link.setAttribute('download', '学生成绩单.csv');
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
  URL.revokeObjectURL(url);
};

// 显示的数据
const displayData = computed(() => {
  return isFiltered.value ? filteredData.value : studentData.value.studentList
})

// 详情表格数据
const detailTableData = computed<DetailTableItem[]>(() => {
  const student = selectedStudent.value;
  if (!student) return [];
  
  return Object.entries(student.scores).map(([sceneName, score]) => ({
    sceneName,
    score: isNaN(score) ? 0.0 : score,
    commitTimes: student.commitTimes[sceneName] || 0
  }))
})

// 获取所有不重复的章节名称
const uniqueChapters = computed(() => {
  const chapters = new Set<string>()
  studentData.value.sceneAverages.forEach(item => {
    if (item.chapterName) {
      chapters.add(item.chapterName)
    }
  })
  return Array.from(chapters)
})

// 根据选择的章节过滤数据（成绩）
const filteredScoreData = computed(() => {
  if (!selectedChapterForScore.value) {
    return studentData.value.sceneAverages
  }
  return studentData.value.sceneAverages.filter(item => 
    item.chapterName === selectedChapterForScore.value
  )
})

// 根据选择的章节过滤数据（提交次数）
const filteredCommitData = computed(() => {
  if (!selectedChapterForCommit.value) {
    return studentData.value.sceneAverages
  }
  return studentData.value.sceneAverages.filter(item => 
    item.chapterName === selectedChapterForCommit.value
  )
})

// 分页后的数据
const paginatedData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return displayData.value.slice(start, end)
})

// 每页条数改变
const handleSizeChange = (newSize: number) => {
  pageSize.value = newSize
  currentPage.value = 1 // 重置到第一页
}

// 页码改变
const handleCurrentChange = (newPage: number) => {
  currentPage.value = newPage
}

// 在搜索和刷新数据时重置页码
const handleSearch = () => {
  if (!searchKeyword.value.trim()) {
    ElMessage.warning('请输入搜索关键词')
    return
  }
  
  const keyword = searchKeyword.value.toLowerCase().trim()
  filteredData.value = studentData.value.studentList.filter(student => 
    student.name.toLowerCase().includes(keyword) ||
    student.studentNo.toLowerCase().includes(keyword)
  )
  
  isFiltered.value = true
  currentPage.value = 1 // 搜索后回到第一页
  ElMessage.success(`找到 ${filteredData.value.length} 条匹配记录`)
}

const clearSearch = () => {
  searchKeyword.value = ''
  isFiltered.value = false
  filteredData.value = []
  currentPage.value = 1 // 清除搜索后回到第一页
  ElMessage.info('已清除筛选条件')
}

// 获取成绩类型
const getScoreType = (score: number) => {
  if (score >= 90) return 'success'
  if (score >= 80) return 'primary'
  if (score >= 60) return 'warning'
  return 'danger'
}

// 获取提交次数百分比
const getSubmissionPercentage = (count: number) => {
  const maxCommitTimes = studentData.value.statistics.maxCommitTimes || 10
  return Math.min((count / maxCommitTimes) * 100, 100)
}

// 获取提交次数颜色
const getSubmissionColor = (count: number) => {
  const maxCommitTimes = studentData.value.statistics.maxCommitTimes || 10
  const percentage = (count / maxCommitTimes) * 100
  
  if (percentage >= 80) return '#f56c6c'
  if (percentage >= 50) return '#e6a23c'
  return '#67c23a'
}

// 初始化图表
const initCharts = () => {
  nextTick(() => {
    if (scoreChartRef.value) {
      scoreChart = echarts.init(scoreChartRef.value)
      updateScoreChart()
    }
    if (commitChartRef.value) {
      commitChart = echarts.init(commitChartRef.value)
      updateCommitChart()
    }
  })
}

const updateScoreChart = () => {
  if (!scoreChart) return

  const xAxisData = filteredScoreData.value.map(item => item.sceneName)
  const seriesData = filteredScoreData.value.map(item => item.averageScore)

  const option: echarts.EChartsOption = {
    title: {
      text: '平均成绩趋势',
      left: 'center',
      textStyle: {
        fontSize: 18, // 增大标题字体
        fontWeight: 'bold'
      },
      padding: [10, 0, 20, 0] // 增加标题下方间距
    },
    tooltip: {
      trigger: 'axis',
      formatter: '{b}: {c}分',
      textStyle: {
        fontSize: 14 // 增大提示框字体
      }
    },
    xAxis: {
      type: 'category',
      data: xAxisData,
      axisLabel: {
        rotate: 45,
        fontSize: 14 // 增大X轴标签字体
      },
      axisLine: {
        lineStyle: {
          width: 2 // 加粗轴线
        }
      }
    },
    yAxis: {
      type: 'value',
      name: '分数',
      min: 0,
      max: 100,
      nameTextStyle: {
        fontSize: 14 // 增大Y轴名称字体
      },
      axisLabel: {
        fontSize: 14 // 增大Y轴标签字体
      },
      axisLine: {
        lineStyle: {
          width: 2 // 加粗轴线
        }
      }
    },
    series: [{
      name: '平均成绩',
      type: 'line',
      data: seriesData,
      smooth: true,
      lineStyle: {
        width: 4, // 加粗线条
        color: '#409EFF'
      },
      itemStyle: {
        color: '#409EFF',
      },
      areaStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: 'rgba(64, 158, 255, 0.6)' },
          { offset: 1, color: 'rgba(64, 158, 255, 0.1)' }
        ])
      }
    }],
    grid: {
      left: '6%',   // 减少左边距
      right: '2%',  // 减少右边距
      bottom: '2%', // 适当减少底边距
      top: '18%',   // 调整顶边距
      containLabel: true
    }
  }

  scoreChart.setOption(option)
}

// 更新提交次数折线图
const updateCommitChart = () => {
  if (!commitChart) return

  const xAxisData = filteredCommitData.value.map(item => item.sceneName)
  const seriesData = filteredCommitData.value.map(item => item.averageCommitTimes)

  const option: echarts.EChartsOption = {
    title: {
      text: '平均提交次数趋势',
      left: 'center',
      textStyle: {
        fontSize: 18, // 增大标题字体
        fontWeight: 'bold'
      },
      padding: [10, 0, 20, 0] // 增加标题下方间距
    },
    tooltip: {
      trigger: 'axis',
      formatter: '{b}: {c}次',
      textStyle: {
        fontSize: 14 // 增大提示框字体
      }
    },
    xAxis: {
      type: 'category',
      data: xAxisData,
      axisLabel: {
        rotate: 45,
        fontSize: 14 // 增大X轴标签字体
      },
      axisLine: {
        lineStyle: {
          width: 2 // 加粗轴线
        }
      }
    },
    yAxis: {
      type: 'value',
      name: '次数',
      nameTextStyle: {
        fontSize: 14 // 增大Y轴名称字体
      },
      axisLabel: {
        fontSize: 14 // 增大Y轴标签字体
      },
      axisLine: {
        lineStyle: {
          width: 2 // 加粗轴线
        }
      }
    },
    series: [{
      name: '平均提交次数',
      type: 'line',
      data: seriesData,
      smooth: true,
      lineStyle: {
        width: 4, // 加粗线条
        color: '#67C23A'
      },
      itemStyle: {
        color: '#67C23A',
      },
      areaStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: 'rgba(103, 194, 58, 0.6)' },
          { offset: 1, color: 'rgba(103, 194, 58, 0.1)' }
        ])
      }
    }],
    grid: {
      left: '7%',   // 减少左边距
      right: '2%',  // 减少右边距
      bottom: '2%', // 适当减少底边距
      top: '18%',   // 调整顶边距
      containLabel: true
    }
  }

  commitChart.setOption(option)
}


// 监听数据变化，更新图表
watch([filteredScoreData, filteredCommitData], () => {
  updateScoreChart()
  updateCommitChart()
})

// 监听窗口大小变化，重新调整图表大小
const handleResize = () => {
  if (scoreChart) scoreChart.resize()
  if (commitChart) commitChart.resize()
}

// 在组件挂载时初始化图表
onMounted(() => {
  refreshData()
  initCharts()
  window.addEventListener('resize', handleResize)
})

// 在组件卸载时销毁图表和事件监听
onUnmounted(() => {
  if (scoreChart) scoreChart.dispose()
  if (commitChart) commitChart.dispose()
  window.removeEventListener('resize', handleResize)
})

// 查看详情
const handleViewDetail = (row: StudentInfo) => {
  selectedStudent.value = row
  detailDialogVisible.value = true
}

// 刷新数据
const refreshData = async () => {
  loading.value = true;
  try {
    setTimeout(async () => {
      const res = await getScoreApi.query() as StudentRecord
      // 处理学生列表的平均成绩NaN问题
      res.studentList = res.studentList.map(student => ({
        ...student,
        averageScore: isNaN(student.averageScore) ? 0.0 : student.averageScore
      }))
      // 处理场景平均成绩NaN问题
      res.sceneAverages = res.sceneAverages.map(scene => ({
        ...scene,
        averageScore: isNaN(scene.averageScore) ? 0.0 : scene.averageScore,
        averageCommitTimes: isNaN(scene.averageCommitTimes) ? 0.0 : scene.averageCommitTimes
      }))
      studentData.value = res

      loading.value = false;
      ElMessage.success('数据刷新成功');

      nextTick(() => {
        updateScoreChart();
        updateCommitChart();
      });
    }, 1000);
  } catch (error) {
    loading.value = false;
    ElMessage.error('数据加载失败');
  }
};

// 初始化加载数据
onMounted(() => {
  refreshData()
})
</script>

<style scoped>
.teacher-dashboard {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.home-icon {
  padding: 8px;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.3s;
  display: flex;
  align-items: center;
  justify-content: center;
}

.home-icon:hover {
  background-color: #f5f7fa;
}

.header-left h2 {
  margin: 0;
  color: #303133;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.filter-info {
  background: #e6f7ff;
  border: 1px solid #91d5ff;
  border-radius: 6px;
  padding: 12px 16px;
  margin-bottom: 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.filter-info span {
  color: #1890ff;
  font-weight: 500;
}

.table-container {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.teacher-download-student-info {
  width: 150px;
  height: 40px;
  background-color: #409eff;
  font-size: 18px;
  color: white;
  border-radius: 6px;
  transition: background-color 0.3s;
}

.teacher-download-student-info:hover {
  background-color: white;
  color: #409eff;
}

.simple-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.actions {
  .el-button {
    padding: 10px 20px;
    font-size: 13px;
    font-weight: 500;
    
    border-radius: 6px;
    background-color: #4CAF50;
    color: white;
    border: none;
    
    box-shadow: 0 2px 4px rgba(76, 175, 80, 0.2);
    
    transition: all 0.2s ease;
    
    &:hover {
      background-color: #3d9140;
      color: white;
      box-shadow: 0 3px 6px rgba(76, 175, 80, 0.3);
      transform: translateY(-1px);
    }
    
    &:active {
      transform: translateY(0);
      box-shadow: 0 1px 2px rgba(76, 175, 80, 0.2);
    }
    
    &:disabled {
      background-color: #a5d6a7;
      color: #fafafa;
      cursor: not-allowed;
      box-shadow: none;
      transform: none;
    }
    
    & .el-icon {
      margin-right: 6px;
      font-size: 14px;
    }
  }
}

.student-name {
  font-weight: bold;
  color: #409EFF;
}

.submission-count {
  font-size: 14px;
  color: #606266;
  margin-top: 8px;
  display: block;
  font-weight: 500;
}

.scene-name {
  font-weight: 500;
  color: #606266;
}

.charts-container {
  margin-top: 30px;
  background: white;
  border-radius: 8px;
  padding: 20px; /* 保留外层内边距，避免图表贴边 */
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.charts-row {
  display: grid;
  grid-template-columns: 1fr;
  gap: 30px;
  margin-top: 20px;
}

.chart-card {
  background: #fafafa;
  border-radius: 8px;
  padding: 20px;
  border: 1px solid #e6e8eb;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.chart-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.chart-content {
  background: white;
  border-radius: 8px;
  padding: 20px;
  border: 1px solid #dcdfe6;
}

.pagination-container {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
  padding: 16px;
  background: white;
  border-top: 1px solid #e6e8eb;
}

:deep(.el-table) {
  max-height: 600px;
  overflow-y: auto;
}

:deep(.el-table__body-wrapper) {
  max-height: 500px;
  overflow-y: auto;
}
</style>