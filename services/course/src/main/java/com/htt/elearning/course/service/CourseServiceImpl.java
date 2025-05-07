package com.htt.elearning.course.service;

import com.htt.elearning.category.pojo.Category;
import com.htt.elearning.category.repository.CategoryRepository;
import com.htt.elearning.course.response.CourseResponse;
import com.htt.elearning.course.response.CourseResponseRedis;
import com.htt.elearning.course.response.TestCourseResponse;
import com.htt.elearning.teacher.TeacherClient;
import com.htt.elearning.teacher.TeacherClient2;
import com.htt.elearning.teacher.response.TeacherResponse;
import com.htt.elearning.teacher.response.TeacherResponseClient;
import com.htt.elearning.user.UserClient;
import com.htt.elearning.course.dto.CourseDTO;
import com.htt.elearning.course.pojo.Course;
import com.htt.elearning.course.repository.CourseRepository;
import com.htt.elearning.tag.pojo.Tag;
import com.htt.elearning.tag.repository.TagRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CategoryRepository categoryRepository;
    private final CourseRepository courseRepository;
    private final TagRepository tagRepository;
    private final TeacherClient teacherClient;
    private final TeacherClient2 teacherClient2;
    private final UserClient userClient;
    private final ModelMapper modelMapper;
    private final HttpServletRequest request;


    @Override
    public Course createCourse(CourseDTO courseDTO) {
        Category existCategory = categoryRepository
                .findById(courseDTO.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Can not find category by id: " + courseDTO.getCategoryId()));

        Tag existTag = tagRepository
                .findById(courseDTO.getTagId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Can not find tag by id: " + courseDTO.getTagId()));
        String token = request.getHeader("Authorization");
        TeacherResponse existingTeacher = Optional.ofNullable(teacherClient.getTeacherById(courseDTO.getTeacherId(), token))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Can not find teacher with id: " + courseDTO.getCategoryId()));

        Course newCourse = Course.builder()
                .name(courseDTO.getName())
                .description(courseDTO.getDescription())
                .image(courseDTO.getImage())
                .price(courseDTO.getPrice())
                .discount(courseDTO.getDiscount())
                .category(existCategory)
                .tag(existTag)
                .teacherId(courseDTO.getTeacherId())
                .build();
        courseRepository.save(newCourse);
        return newCourse;
    }

    @Override
    public TestCourseResponse getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Can not find course with id : " + id));

        TeacherResponseClient teacher = teacherClient2.getInformationOneTeacher(course.getTeacherId());

        return TestCourseResponse.fromCourse(course, teacher);
    }

    public List<Course> searchCourses(String keyword) {
        return courseRepository.searchByKeyword(keyword);
    }

    @Override
    public Page<Course> getAllCourses(Pageable pageable) {
        return courseRepository.findAll(pageable);
    }

    @Override
    public List<Course> getAllCourseName(){
        return courseRepository.findAll();
    }

    @Override
    public List<Course> getCoursesByCategoryId(Long categoryId) {
        Category existingCate = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Can not find category by id: " + categoryId));
        if (categoryId != null) {
            return courseRepository.findByCategoryId(categoryId);
        }
        else {
            return courseRepository.findAll();
        }
    }

    @Override
    public Page<Course> getCoursesByCategoryIdPage(Pageable pageable, Long categoryId) {
        if (categoryId != null) {
            return courseRepository.findByCategoryId(categoryId, pageable);
        }
        else {
            return courseRepository.findAll(pageable);
        }
    }

    @Override
    public Page<Course> getCoursesByPrice(Float minPrice, Float maxPrice, Pageable pageable) {
        if (minPrice != null && maxPrice != null) {
            return courseRepository.getCoursesByPrice(minPrice, maxPrice, pageable);
        }
        else {
            return courseRepository.findAll(pageable);
        }
    }

    @Override
    public Page<Course> getCoursesByTeacherId(Long teacherId, Pageable pageable) {
        String token = request.getHeader("Authorization");
        Long userId = userClient.getUserIdByUsernameClient(token);

        TeacherResponse existingTeacher = Optional.ofNullable(teacherClient.getTeacherById(teacherId, token))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Can not find teacher with id: " + teacherId));

        if (existingTeacher.getUserId() != userId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden");
        }

        return courseRepository.findByTeacherId(teacherId, pageable);
    }

    @Override
    public Page<TestCourseResponse> getTestCoursesByTeacherId(Long teacherId, Pageable pageable) {
        String token = request.getHeader("Authorization");
        Long userId = userClient.getUserIdByUsernameClient(token);

        TeacherResponse existingTeacher = Optional.ofNullable(teacherClient.getTeacherById(teacherId, token))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Can not find teacher with id: " + teacherId));

//        if (existingTeacher.getUserId() != userId) {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden");
//        }

        Page<Course> coursesPage = courseRepository.findByTeacherId(teacherId, pageable);
        List<Course> courses = coursesPage.getContent();

        // 2. Lấy danh sách teacherIds
        List<Long> teacherIds = courses.stream()
                .map(Course::getTeacherId)
                .distinct()
                .collect(Collectors.toList());

        // 3. Gọi sang TeacherService để lấy thông tin teacher
        List<TeacherResponseClient> teachers = teacherClient2.getInformationTeacher(teacherIds);
        Map<Long, TeacherResponseClient> teacherMap = teachers.stream()
                .collect(Collectors.toMap(TeacherResponseClient::getId, Function.identity()));

        // 4. Map Course -> TestCourseResponse
        List<TestCourseResponse> courseResponses = courses.stream()
                .map(course -> {
                    TeacherResponseClient teacher = teacherMap.get(course.getTeacherId());
                    return TestCourseResponse.fromCourse(course, teacher);
                })
                .collect(Collectors.toList());

        // 5. Trả về Page<TestCourseResponse>
        return new PageImpl<>(courseResponses, pageable, coursesPage.getTotalElements());
    }

    @Override
    public Course updateCourse(Long id, CourseDTO courseDTO) {
        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Can not find course with id : " + id));
        if(existingCourse != null){
            //copy thuộc tính từ DTO sang
            Category existCategory = categoryRepository.findById(courseDTO.getCategoryId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Can not find category by id: " + courseDTO.getCategoryId()));
            String token = request.getHeader("Authorization");
            TeacherResponse existingTeacher = Optional.ofNullable(teacherClient.getTeacherById(courseDTO.getTeacherId(), token))
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Can not find teacher with id: " + courseDTO.getCategoryId()));

            existingCourse.setName(courseDTO.getName());
            existingCourse.setDescription(courseDTO.getDescription());
            existingCourse.setPrice(courseDTO.getPrice());
            existingCourse.setDiscount(courseDTO.getDiscount());
            existingCourse.setCategory(existCategory);
            existingCourse.setTeacherId(courseDTO.getTeacherId());
            existingCourse.setUpdatedDate(new Date());

            return courseRepository.save(existingCourse);
            //sử dụng ModelMapper
        }
        return null;
    }

    @Override
    public void deleteCourse(Long id) {
        Optional<Course> optionalCourse = courseRepository.findById(id);
        optionalCourse.ifPresent(courseRepository::delete);
    }

    @Override
    public boolean existByName(String name) {
        return courseRepository.existsByName(name);
    }

//    course - client
    @Override
    public CourseResponse getCourseByIdClient(Long courseId){
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Can not find course with id : " + courseId));

        return CourseResponse.fromCourse(course);
    }

    @Override
    public List<Long> searchCourseIdsByNameClient(String keyword){
        return courseRepository.searchCourseIdsByName(keyword);
    }

    @Override
    public Page<CourseResponseRedis> getAllCoursesRedisClient(Pageable pageable, String keyword, Long categoryId) {
        Page<Course> coursePage = courseRepository.searchCoursesRedis(categoryId, keyword, pageable);
        List<Course> courses = coursePage.getContent();

        // 2. Lấy danh sách teacherIds
        List<Long> teacherIds = courses.stream()
                .map(Course::getTeacherId)
                .distinct()
                .collect(Collectors.toList());

        // 3. Gọi sang TeacherService để lấy thông tin teacher
        List<TeacherResponseClient> teachers = teacherClient2.getInformationTeacher(teacherIds);
        Map<Long, TeacherResponseClient> teacherMap = teachers.stream()
                .collect(Collectors.toMap(TeacherResponseClient::getId, Function.identity()));


        List<CourseResponseRedis> courseResponseRedis = courses.stream()
                .map(course -> {
                    TeacherResponseClient teacher = teacherMap.get(course.getTeacherId());
                    return CourseResponseRedis.fromCourse(course, teacher);
                })
                .collect(Collectors.toList());

        // 5. Trả về Page<TestCourseResponse>
        return new PageImpl<>(courseResponseRedis, pageable, coursePage.getTotalElements());
    }

    @Override
    public Page<TestCourseResponse> testCourses(Pageable pageable) {    // 1. Phân trang Course
        Page<Course> coursePage = courseRepository.findAll(pageable);
        List<Course> courses = coursePage.getContent();

        // 2. Lấy danh sách teacherIds
        List<Long> teacherIds = courses.stream()
                .map(Course::getTeacherId)
                .distinct()
                .collect(Collectors.toList());

        // 3. Gọi sang TeacherService để lấy thông tin teacher
        List<TeacherResponseClient> teachers = teacherClient2.getInformationTeacher(teacherIds);
        Map<Long, TeacherResponseClient> teacherMap = teachers.stream()
                .collect(Collectors.toMap(TeacherResponseClient::getId, Function.identity()));

        // 4. Map Course -> TestCourseResponse
        List<TestCourseResponse> courseResponses = courses.stream()
                .map(course -> {
                    TeacherResponseClient teacher = teacherMap.get(course.getTeacherId());
                    return TestCourseResponse.fromCourse(course, teacher);
                })
                .collect(Collectors.toList());

        // 5. Trả về Page<TestCourseResponse>
        return new PageImpl<>(courseResponses, pageable, coursePage.getTotalElements());
    }

    @Override
    public TestCourseResponse getTestCourseResponseByCourseId(Long courseId){
        Course course = courseRepository.getCourseById(courseId);
        TeacherResponseClient teacherResponseClient = teacherClient2.getInformationOneTeacher(course.getTeacherId());
        return TestCourseResponse.fromCourse(course, teacherResponseClient);
    }

    @Override
    public List<TestCourseResponse> getTestCourseResponseByCourseIds(List<Long> courseIds) {
        List<Course> courses = courseRepository.findAllById(courseIds);
        List<Long> teacherIds = courses.stream()
                .map(Course::getTeacherId)
                .distinct()
                .collect(Collectors.toList());

        // 3. Gọi sang TeacherService để lấy thông tin teacher
        List<TeacherResponseClient> teachers = teacherClient2.getInformationTeacher(teacherIds);
        Map<Long, TeacherResponseClient> teacherMap = teachers.stream()
                .collect(Collectors.toMap(TeacherResponseClient::getId, Function.identity()));

        // 4. Map Course -> TestCourseResponse
        List<TestCourseResponse> courseResponses = courses.stream()
                .map(course -> {
                    TeacherResponseClient teacher = teacherMap.get(course.getTeacherId());
                    return TestCourseResponse.fromCourse(course, teacher);
                })
                .collect(Collectors.toList());
        return courseResponses;
    }
}
