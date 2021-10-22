package com.example.skgym.Interfaces

import com.example.skgym.data.Plan

interface PlanFinalCallback {
    fun getCurrentPlan(plan: Plan)
}