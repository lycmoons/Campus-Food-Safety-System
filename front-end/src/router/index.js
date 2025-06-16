import {createRouter, createWebHistory} from "vue-router";
import {getRole, getToken} from "@/net/index.js";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        // 欢迎界面
        {
            path:'/',
            name: 'welcome',
            component: () => import('@/views/WelcomeView.vue'),
            children: [
                {
                    path: '',
                    name: 'welcome-login',
                    component: () => import('@/views/welcome/LoginPage.vue')
                },
                {
                    path: 'register',
                    name: 'welcome-register',
                    component: () => import('@/views/welcome/RegisterPage.vue')
                },
                {
                    path: 'reset',
                    name: 'welcome-reset',
                    component: () => import('@/views/welcome/ResetPage.vue')
                }
            ]
        },

        // 用户界面
        {
            path: '/index',
            name: 'index',
            component: () => import('@/views/IndexView.vue'),
            children: [
                // 首页
                {
                    path: '',
                    name: 'home',
                    component: () => import('@/views/index/HomePage.vue')
                },

                // 留言板
                {
                    path: 'post',
                    name: 'post',
                    component: () => import('@/views/index/PostPage.vue')
                },

                // 食品溯源
                {
                    path: 'trace',
                    name: 'trace',
                    component: () => import('@/views/index/FoodTracePage.vue')
                },

                // 投诉
                {
                    path: 'complaint',
                    name: 'complaint',
                    component: () => import('@/views/index/ComplaintPage.vue'),
                    children: [
                        // 我的投诉
                        {
                            path: '',
                            name: 'my-complaint',
                            component: () => import('@/views/index/complaint/AllComplaint.vue')
                        },

                        // 新建投诉
                        {
                            path: 'new-complaint',
                            name: 'new-complaint',
                            component: () => import('@/views/index/complaint/NewComplaint.vue')
                        }
                    ]
                },

                // 我的
                {
                    path: 'account',
                    name: 'account',
                    component: () => import('@/views/index/AccountPage.vue'),
                    children: [
                        // 我的账号
                        {
                            path: '',
                            name: 'my-account',
                            component: () => import('@/views/index/account/PersonalInfoPage.vue')
                        },

                        // 我的消息
                        {
                            path: 'my-message',
                            name: 'my-message',
                            component: () => import('@/views/index/account/PersonalMessagePage.vue')
                        }
                    ]
                },

                // 智能助手
                {
                    path: 'assistant',
                    name: 'assistant',
                    component: () => import('@/views/index/AssistantPage.vue')
                }
            ]
        },

        // 管理员界面
        {
            path: '/admin',
            name: 'admin',
            component: () => import('@/views/AdminView.vue'),
            children: [
                //食品信息管理界面
                {
                    path: 'foodinfoManage',
                    name: 'foodinfoManage',
                    component: () => import('@/views/admin/FoodInfoManagePage.vue'),
                    children: [
                        {
                            path: 'foodinfoList',
                            name: 'foodinfoList',
                            component: () => import('@/views/admin/foodinfo/FoodInfoListPage.vue'),
                        },
                        {
                            path: 'foodinfoDetail/:id/:name/:photo_url',
                            name: 'foodinfoDetail',
                            component: () => import('@/views/admin/foodinfo/FoodInfoDetailPage.vue'),
                        },
                        {
                            path: 'foodproduct/:id/:name/:photo_url',
                            name: 'foodproduct',
                            component: () => import('@/views/admin/foodinfo/FoodProduction.vue'),
                        },
                        {
                            path: 'foodtrans/:id/:name/:photo_url',
                            name: 'foodtrans',
                            component: () => import('@/views/admin/foodinfo/FoodTrans.vue'),
                        },
                        {
                            path: 'inputFoodinfo',
                            name: 'inputFoodinfo',
                            component: () => import('@/views/admin/foodinfo/InputFoodInfoPage.vue'),
                        },
                    ]
                },

                // 用户投诉受理界面
                {
                    path: 'complaintManage',
                    name: 'complaintManage',
                    component: () => import('@/views/admin/ComplaintManagePage.vue'),
                    children: [
                        {
                            path: 'complaintList',
                            name: 'complaintList',
                            component: () => import('@/views/admin/complaint/ComplaintListPage.vue'),
                        },
                        {
                            path: 'complaintAcceptance',
                            name: 'complaintAcceptance',
                            component: () => import('@/views/admin/complaint/ComplaintAcceptancePage.vue'),
                        },
                    ]
                },

                // 推送新闻界面
                {
                    path: 'articleManage',
                    name: 'articleManage',
                    component: () => import('@/views/admin/ArticleManagePage.vue'),
                    children: [
                        {
                            path: 'articleList',
                            name: 'articleList',
                            component: () => import('@/views/admin/article/ArticleListPage.vue')
                        },
                        {
                            path: 'articleDetail',
                            name: 'articleDetail',
                            component: () => import('@/views/admin/article/ArticleDetailPage.vue')
                        },
                        {
                            path: 'createArticle',
                            name: 'createArticle',
                            component: () => import('@/views/admin/article/CreateArticlePage.vue'),
                        },
                    ]
                },

                // 推送知识界面
                {
                    path: 'knowledgeManage',
                    name: 'knowledgeManage',
                    component: () => import('@/views/admin/ArticleManagePage.vue'),
                    children: [
                        {
                            path: 'knowledgeList',
                            name: 'knowledgeList',
                            component: () => import('@/views/admin/knowledge/KnowledgeListPage.vue')
                        },
                        {
                            path: 'knowledgeDetail',
                            name: 'knowledgeDetail',
                            component: () => import('@/views/admin/knowledge/KnowledgeDetailPage.vue')
                        },
                        {
                            path: 'createKnowledge',
                            name: 'createKnowledge',
                            component: () => import('@/views/admin/knowledge/CreateKnowledgePage.vue'),
                        },
                    ]
                },

                // 管理员日志界面
                {
                    path: 'log',
                    name: 'adminLog',
                    component: () => import('@/views/admin/AdminLogPage.vue'),
                },

                // 个人信息
                {
                    path: 'profile',
                    name: 'admin-profile',
                    component: () => import('@/views/admin/AdminProfileManagePage.vue'),
                },
            ]
        },

        // 显示所有新闻的界面
        {
            path: '/all-news',
            name: 'all-news',
            component: () => import('@/views/AllNews.vue')
        },

        // 显示所有知识的界面
        {
            path: '/all-knowledge',
            name: 'all-knowledge',
            component: () => import('@/views/AllKnowledge.vue')
        },

        // 显示所有帖子的界面
        {
            path: '/all-post',
            name: 'all-post',
            component: () => import('@/views/AllPost.vue')
        },

        // 新闻的详细显示界面
        {
            path: '/news/:id/:title/:content/:photo_url/:date',
            name: 'news',
            component: () => import('@/views/NewsView.vue')
        },

        // 知识的详细显示界面
        {
            path: '/knowledge/:id/:category/:title/:content/:photo_url/:date',
            name: 'knowledge',
            component: () => import('@/views/KnowledgeView.vue')
        },

        // 帖子的详细显示界面
        {
            path: '/postDetail/:id/:title/:username/:date/:content/:photo_url',
            name: 'postDetail',
            component: () => import('@/views/PostView.vue')
        },

        // 显示食品生产信息
        {
            path: '/production/:id/:name/:photo_url',
            name: 'production',
            component: () => import('@/views/food/ProductionView.vue')
        },

        // 显示食品运输信息
        {
            path: '/production/:id/:name/:photo_url',
            name: 'transportation',
            component: () => import('@/views/food/TransportationView.vue')
        },

        // 显示食品消费信息
        {
            path: '/production/:id/:name/:photo_url',
            name: 'consumer',
            component: () => import('@/views/food/ConsumerView.vue')
        }
    ]
})

// 配置路由守卫
router.beforeEach((to, from, next) => {
    const token = getToken()
    // 已经登录后不能再访问登录界面
    // 选择记住密码后，token被存储在localStorage中
    // 下次登录是可以获取的，故不能再访问登录界面，直接重定向到主界面，完成记住密码的功能
    if(to.name.startsWith('welcome-') && token !== null) {
        let role = getRole()
        if (role === 'user'){
            next('/index')  // 重定向
        }
        else if (role === 'admin'){
            next('/admin')
        }
    }

    // 在没登录的情况下访问用户界面或管理员界面，直接跳转到登录界面
    else if((to.fullPath.startsWith('/index') || to.fullPath.startsWith('/admin')) && token === null){
        next('/')  // 重定向
    }

    else {  // 正常情况下
        next()   // 无需重定向
    }

})

export default router

