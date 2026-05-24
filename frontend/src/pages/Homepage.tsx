export default function Homepage() {
  return (
    <div className="w-full h-full">
      {/* Body1*/}
        <div className='relative h-96 w-full'>
        <img src="/Assets/hp3.jpg" className='h-96 w-screen object-cover' alt="" />
        
        <div className='absolute inset-0 p-20 bg-black bg-opacity-60'>
            <p className='text-white text-4xl font-bold'>Library Management System</p>
            <p className='text-white text-xl font-semibold mt-20'>A software that helps organize and manage the operations of a library</p>
        </div>
        </div>

        {/* Body2*/}
        <div className='h-screen w-full bg-gray-200 flex flex-col pt-10 gap-y-10'>
            
            <h1 className='text-3xl text-black text-center font-bold'>Introduction</h1>

            <div className='flex flex-row text-center justify-center gap-x-[100px]'>
                <div className='w-[500px] flex flex-col gap-y-4'>
                    <h2 className='text-xl font-semibold text-orange-600'>Our Project</h2>
                    <p>Our LMS (Library Management System) is a software that helps manage library resources, including cataloging, borrowing, returning, and circulation tracking. This software supports the systematic and accurate management of library operations, making it easy to store book information and track the status of borrowed books</p>

                </div>

                <div className='w-[500px] flex flex-col gap-y-4'>
                    <h2 className='text-xl font-semibold text-orange-600'>Purposes</h2>
                    <div className='flex flex-col gap-y-4'>
                    <ul>Streamline information retrieval: Provide a user-friendly interface that enables readers to quickly locate and access desired books.</ul>
                    <ul>Optimize resource management: Assist librarians in managing inventory, tracking circulation, and identifying popular materials</ul>
                    </div>
                </div>
            </div>

            <h1 className='text-3xl text-black text-center font-bold'>Team Members</h1>
            
            <div className='flex justify-center gap-x-[200px]'>
            <ol className="list-decimal pl-9 flex flex-col  gap-y-1 ">
                <li>Nguyen Mach Khang Huy</li>
                <li>Tran Nguyen Phuc</li>
                <li>Tran Quoc Bao</li>
                <li>Nguyen Minh Viet</li>
                <li>Bui Cong Vinh</li>
                <li>Nguyen Bach Dong Phuong</li>
                <li>Nguyen Thi Quynh Nga</li>
                <li>Le Minh Duy</li>
            </ol>

            <ul className='flex flex-col gap-y-1'>
                <li>Team Leader</li>
                <li>Vice Leader</li>
                <li>Team Member</li>
                <li>Team Member</li>
                <li>Team Member</li>
                <li>Team Member</li>
                <li>Team Member</li>
                <li>Team Member</li>
            </ul>
                
            </div>

        </div>

        {/*End Body2*/}

    </div>
  )
}
