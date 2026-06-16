package com.example.testunitairetp1

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import io.kotest.core.spec.style.FunSpec

private const val BASE_PACKAGE = "com.example.testunitairetp1"
private const val DOMAIN_PACKAGE = "$BASE_PACKAGE.domain.."
private const val DRIVING_PACKAGE = "$BASE_PACKAGE.infrastructure.driving.."
private const val DRIVEN_PACKAGE = "$BASE_PACKAGE.infrastructure.driven.."
private const val APPLICATION_PACKAGE = "$BASE_PACKAGE.infrastructure.application.."

class HexagonalArchitectureTest : FunSpec({

    val importedClasses: JavaClasses = ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages(BASE_PACKAGE)

    test("the domain should not depend on the infrastructure") {
        noClasses()
            .that().resideInAPackage(DOMAIN_PACKAGE)
            .should().dependOnClassesThat().resideInAnyPackage(DRIVING_PACKAGE, DRIVEN_PACKAGE, APPLICATION_PACKAGE)
            .check(importedClasses)
    }

    test("the driving adapters should not depend on the driven adapters or the application wiring") {
        noClasses()
            .that().resideInAPackage(DRIVING_PACKAGE)
            .should().dependOnClassesThat().resideInAnyPackage(DRIVEN_PACKAGE, APPLICATION_PACKAGE)
            .check(importedClasses)
    }

    test("the driven adapters should not depend on the driving adapters or the application wiring") {
        noClasses()
            .that().resideInAPackage(DRIVEN_PACKAGE)
            .should().dependOnClassesThat().resideInAnyPackage(DRIVING_PACKAGE, APPLICATION_PACKAGE)
            .check(importedClasses)
    }
})
