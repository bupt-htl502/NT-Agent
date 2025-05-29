export default [
    {
        path: '/experiment/10002',
        name: 'experiment_10002',
        component: () => import('@/views/NetworkProtocolAnalysis/Wireshark&Tshark.vue'),
        props: route => ({ title: route.query.title || 'Default Title', id: '10002'}),
        meta: { requiresUnlock: false }
    },
    {
        path: '/experiment/10004',
        name: 'experiment_10004',
        component: () => import('@/views/NetworkProtocolAnalysis/Capturefilter.vue'),
        props: route => ({ title: route.query.title || 'Default Title', id: '10004'}),
        meta: { requiresUnlock: true }
    },
    {
        path: '/experiment/10005',
        name: 'experiment_10005',
        component: () => import('@/views/NetworkProtocolAnalysis/displayfilter.vue'),
        props: route => ({ title: route.query.title || 'Default Title', id: '10005' }),
        meta: { requiresUnlock: true }
    },
    {
        path: '/experiment/10007',
        name: 'experiment_10007',
        component: () => import('@/views/NetworkProtocolAnalysis/WiresharkTrafficAnalysis.vue'),
        props: route => ({ title: route.query.title || 'Default Title', id: '10007' }),
        meta: { requiresUnlock: true }
    },
    {
        path: '/experiment/10009',
        name: 'experiment_10009',
        component: () => import('@/views/NetworkProtocolAnalysis/Decryptionofencrypteddatapackets.vue'),
        props: route => ({ title: route.query.title || 'Default Title', id: '10009' }),
        meta: { requiresUnlock: true }
    },
    {
        path: '/experiment/20002',
        name: 'experiment_20002',
        component: () => import('@/views/Trafficfileprocessing/Packetcleaning.vue'),
        props: route => ({ title: route.query.title || 'Default Title', id: '20002' }),
        meta: { requiresUnlock: true }
    },
    {
        path: '/experiment/20003',
        name: 'experiment_20003',
        component: () => import('@/views/Trafficfileprocessing/Datapackettimecontinuityverification.vue'),
        props: route => ({ title: route.query.title || 'Default Title', id: '20003' }),
        meta: { requiresUnlock: false }
    },
    {
        path: '/experiment/20005',
        name: 'experiment_20005',
        component: () => import('@/views/Trafficfileprocessing/Filterdatapacketsaccording2specificprotocols.vue'),
        props: route => ({ title: route.query.title || 'Default Title', id: '20005' }),
        meta: { requiresUnlock: true }
    },
    {
        path: '/experiment/20006',
        name: 'experiment_20006',
        component: () => import('@/views/Trafficfileprocessing/Dividedatapacketsintoquintuples.vue'),
        props: route => ({ title: route.query.title || 'Default Title', id: '20006' }),
        meta: { requiresUnlock: true }
    },
    {
        path: '/experiment/30002',
        name: 'experiment_30002',
        component: () => import('@/views/Interactionbehavioranalysis/Analysisofnormalbusinesstraffic.vue'),
        props: route => ({ title: route.query.title || 'Default Title', id: '30002' }),
        meta: { requiresUnlock: true }
    },
    {
        path: '/experiment/30004',
        name: 'experiment_30004',
        component: () => import('@/views/Interactionbehavioranalysis/DNSTunnelTrafficResolution.vue'),
        props: route => ({ title: route.query.title || 'Default Title', id: '30004' }),
        meta: { requiresUnlock: true }
    },
    {
        path: '/experiment/30005',
        name: 'experiment_30005',
        component: () => import('@/views/Interactionbehavioranalysis/DOHTunnelTrafficResolution.vue'),
        props: route => ({ title: route.query.title || 'Default Title', id: '30005' }),
        meta: { requiresUnlock: true }
    },
    {
        path: '/experiment/30007',
        name: 'experiment_30007',
        component: () => import('@/views/Interactionbehavioranalysis/Malicioustrafficanalysis.vue'),
        props: route => ({ title: route.query.title || 'Default Title', id: '30007' }),
        meta: { requiresUnlock: true }
    },
    {
        path: '/experiment/40002',
        name: 'experiment_40002',
        component: () => import('@/views/Feature/CICFlowMeterFeature.vue'),
        props: route => ({ title: route.query.title || 'Default Title', id: '40002' }),
        meta: { requiresUnlock: true }
    },
    {
        path: '/experiment/40003',
        name: 'experiment_40003',
        component: () => import('@/views/Feature/dpktFeature.vue'),
        props: route => ({ title: route.query.title || 'Default Title', id: '40003' }),
        meta: { requiresUnlock: true }
    },
    {
        path: '/experiment/40004',
        name: 'experiment_40004',
        component: () => import('@/views/Feature/Numerical&normalfeature.vue'),
        props: route => ({ title: route.query.title || 'Default Title', id: '40004' }),
        meta: { requiresUnlock: true }
    },
    {
        path: '/experiment/40007',
        name: 'experiment_40007',
        component: () => import('@/views/Feature/Byteoffsetfieldextraction.vue'),
        props: route => ({ title: route.query.title || 'Default Title', id: '40007' }),
        meta: { requiresUnlock: true }
    },
    {
        path: '/experiment/40008',
        name: 'experiment_40008',
        component: () => import('@/views/Feature/Pysharkfieldextraction.vue'),
        props: route => ({ title: route.query.title || 'Default Title', id: '40008' }),
        meta: { requiresUnlock: true }
    },
    {
        path: '/experiment/40011',
        name: 'experiment_40011',
        component: () => import('@/views/Feature/Bytes2Images.vue'),
        props: route => ({ title: route.query.title || 'Default Title', id: '40011' }),
        meta: { requiresUnlock: true }
    },
    {
        path: '/experiment/40012',
        name: 'experiment_40012',
        component: () => import('@/views/Feature/Convertbytesintoembeddingvectors.vue'),
        props: route => ({ title: route.query.title || 'Default Title', id: '40012' }),
        meta: { requiresUnlock: true }
    },
    {
        path: '/experiment/50002',
        name: 'experiment_50002',
        component: () => import('@/views/AIEmpoweredTrafficAnalysis/Basedonstatisticalmaliciousfeatures.vue'),
        props: route => ({ title: route.query.title || 'Default Title', id: '50002' }),
        meta: { requiresUnlock: true }
    },
    {
        path: '/experiment/50003',
        name: 'experiment_50003',
        component: () => import('@/views/AIEmpoweredTrafficAnalysis/Basedonmalicioustrafficbytes.vue'),
        props: route => ({ title: route.query.title || 'Default Title', id: '50003' }),
        meta: { requiresUnlock: true }
    },
    {
        path: '/experiment/50005',
        name: 'experiment_50005',
        component: () => import('@/views/AIEmpoweredTrafficAnalysis/Usingstatisticalfeaturesforencryptedtrafficclassification.vue'),
        props: route => ({ title: route.query.title || 'Default Title', id: '50005' }),
        meta: { requiresUnlock: true }
    },
    {
        path: '/experiment/50006',
        name: 'experiment_50006',
        component: () => import('@/views/AIEmpoweredTrafficAnalysis/Usingmixedinputforencryptedtrafficclassification.vue'),
        props: route => ({ title: route.query.title || 'Default Title', id: '50006' }),
        meta: { requiresUnlock: true }
    },
];